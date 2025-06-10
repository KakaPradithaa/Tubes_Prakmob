package com.example.bengkelappclient.ui.theme.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.databinding.ActivityAddServiceBinding
import com.example.bengkelappclient.ui.service.ServiceListActivity
import com.example.bengkelappclient.ui.service.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceBinding
    private var imageUri: Uri? = null
    private val viewModel: ServiceViewModel by viewModels()
    private var serviceToUpdate: Service? = null

    // Launcher untuk memilih gambar dari galeri
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun setupActionListeners() {
        // Menambahkan fungsi untuk tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data service dari intent
        serviceToUpdate = intent.getParcelableExtra(EXTRA_SERVICE)

        // Cek apakah ini mode update atau tambah baru
        if (serviceToUpdate != null) {
            setupUpdateMode()
        } else {
            // Mode Tambah Baru
            binding.tvHeaderTitle.text = "Tambah Service Baru"
            binding.btnSubmit.text = "Tambah"
        }

        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {
            submitForm()
        }

        // Setup listener untuk tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        observeResult()
        observeUpdateResult()
    }

    private fun setupUpdateMode() {
        binding.tvHeaderTitle.text = "Update Service"
        binding.btnSubmit.text = "Update"

        serviceToUpdate?.let { service ->
            binding.etName.setText(service.name)
            binding.etDescription.setText(service.description)
            binding.etPrice.setText(service.price.toString())

            // Pastikan URL gambar lengkap dan benar
            val fullImageUrl = "http://10.0.2.2:8000/uploads/services/" + service.img
            Glide.with(this)
                .load(fullImageUrl)
                .placeholder(R.drawable.ic_placeholder) // Gambar placeholder jika gagal memuat
                .into(binding.ivPreview)
        }
    }

    private fun submitForm() {
        val name = binding.etName.text.toString().trim()
        val desc = binding.etDescription.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Nama dan harga wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null
        imageUri?.let {
            val file = createTempFileFromUri(it)
            file?.let {
                val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                imagePart = MultipartBody.Part.createFormData("img", it.name, requestFile)
            }
        }

        if (serviceToUpdate != null) {
            // Panggil fungsi update dari ViewModel
            viewModel.updateService(serviceToUpdate!!.id, nameBody, descBody, priceBody, imagePart)
        } else {
            // Panggil fungsi tambah dari ViewModel
            viewModel.addService(nameBody, descBody, priceBody, imagePart)
        }
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp_image", ".jpg", cacheDir)
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun observeResult() {
        viewModel.serviceResult.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    Toast.makeText(this, "Menambahkan layanan...", Toast.LENGTH_SHORT).show()
                }
                is ServiceResult.Success -> {
                    Toast.makeText(this, "Service berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ServiceListActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                is ServiceResult.Error -> {
                    val errorMessage = result.message ?: "Terjadi kesalahan."
                    Toast.makeText(this, "Gagal menambahkan service: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeUpdateResult() {
        viewModel.updateResult.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    Toast.makeText(this, "Memperbarui layanan...", Toast.LENGTH_SHORT).show()
                }
                is ServiceResult.Success -> {
                    Toast.makeText(this, "Service berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ServiceListActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                is ServiceResult.Error -> {
                    val errorMessage = result.message ?: "Terjadi kesalahan."
                    Toast.makeText(this, "Gagal memperbarui: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Companion object harus berada di level class, bukan di dalam function
    companion object {
        const val EXTRA_SERVICE = "extra_service"
        const val EXTRA_SERVICE_ID = "extra_service_id" // Jika masih diperlukan di tempat lain
    }
}