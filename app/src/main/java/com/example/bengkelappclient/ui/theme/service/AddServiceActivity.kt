package com.example.bengkelappclient.ui.service

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.databinding.ActivityAddServiceBinding
import com.example.bengkelappclient.data.model.ServiceResult // <--- Pastikan import ini ada
import com.example.bengkelappclient.util.toRequestBody // <--- Pastikan import ini ada (dari file Utils.kt yang baru dibuat)
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class AddServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceBinding
    private var imageUri: Uri? = null
    // Menggunakan by viewModels() untuk menginisialisasi ViewModel dengan Hilt
    private val viewModel: ServiceViewModel by viewModels()

    // ActivityResultLauncher untuk memilih gambar dari galeri
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        binding.ivPreview.setImageURI(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener untuk tombol "Choose Image"
        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*") // Membuka galeri untuk memilih gambar
        }

        // Listener untuk tombol "Submit"
        binding.btnSubmit.setOnClickListener {
            submitForm() // Memanggil fungsi submitForm saat tombol diklik
        }

        // Mengamati hasil operasi dari ViewModel
        observeResult()
    }

    /**
     * Handles the form submission logic, including input validation and calling the ViewModel.
     */
    private fun submitForm() {
        val name = binding.etName.text.toString().trim()
        val desc = binding.etDescription.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Nama dan harga wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = name.toRequestBody()
        val descBody = desc.toRequestBody()
        val priceBody = price.toRequestBody()

        var imagePart: MultipartBody.Part? = null

        imageUri?.let {
            val file = createTempFileFromUri(it) // ✅ gunakan fungsi baru
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("img", file.name, requestFile)
        }

        viewModel.addService(nameBody, descBody, priceBody, imagePart)
    }

    private fun createTempFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }


    /**
     * Observes the serviceResult LiveData from the ViewModel to update UI based on operation status.
     */
    private fun observeResult() {
        viewModel.serviceResult.observe(this) { result ->
            // Pastikan result tidak null sebelum diproses
            result?.let {
                when (it) {
                    is ServiceResult.Loading -> {
                        // Tampilkan loading indicator (opsional)
                        Toast.makeText(this, "Menambahkan layanan...", Toast.LENGTH_SHORT).show()
                    }
                    is ServiceResult.Success -> {
                        // Jika operasi sukses
                        Toast.makeText(this, "Service berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        // Pindah ke ServiceListActivity setelah sukses
                        startActivity(Intent(this, ServiceListActivity::class.java)) // <--- Pastikan ServiceListActivity ada dan di-import
                        finish() // Tutup AddServiceActivity
                    }
                    is ServiceResult.Error -> {
                        // Jika terjadi error
                        val errorMessage = it.message ?: "Terjadi kesalahan yang tidak diketahui."
                        Toast.makeText(this, "Gagal menambahkan service: $errorMessage", Toast.LENGTH_LONG).show()
                        // Opsional: Log error untuk debugging
                        it.exception.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * Helper function to get the real file path from a content URI.
     * This is needed for creating a File object from the Uri obtained from image picker.
     *
     * @param uri The content URI of the image.
     * @return The real file path as a String.
     */
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val path = cursor.getString(idx)
            cursor.close()
            path
        } else {
            // Fallback for cases where MediaStore.Images.Media.DATA is not available
            uri.path ?: ""
        }
    }
}
