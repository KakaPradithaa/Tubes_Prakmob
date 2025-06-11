package com.example.bengkelappclient.ui.theme.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.databinding.ActivityAddServiceBinding
import com.example.bengkelappclient.ui.theme.service.ServiceListActivity
import com.example.bengkelappclient.ui.service.ServiceViewModel
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceBinding
    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private val viewModel: ServiceViewModel by viewModels()
    private var serviceToUpdate: Service? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.ivPreview.setImageURI(it)
            imageFile = createTempFileFromUri(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceToUpdate = intent.getParcelableExtra(EXTRA_SERVICE)

        if (serviceToUpdate != null) {
            setupUpdateMode()
        } else {
            binding.tvHeaderTitle.text = "Tambah Service Baru"
            binding.btnSubmit.text = "Tambah"
        }

        setupActionListeners()
        observeViewModel()
    }

    private fun setupUpdateMode() {
        binding.tvHeaderTitle.text = "Update Service"
        binding.btnSubmit.text = "Update"

        serviceToUpdate?.let { service ->
            binding.etName.setText(service.name)
            binding.etDescription.setText(service.description)
            binding.etPrice.setText(service.price.toString())
            val fullImageUrl = "http://10.0.2.2:8000/uploads/services/" + service.img
            Glide.with(this).load(fullImageUrl).placeholder(R.drawable.ic_placeholder).into(binding.ivPreview)
        }
    }

    private fun setupActionListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.btnSubmit.setOnClickListener {
            submitForm()
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

        if (serviceToUpdate != null) {
            viewModel.updateService(serviceToUpdate!!.id, name, desc, price, imageFile)
        } else {
            viewModel.createService(name, desc, price, imageFile)
        }
    }

    private fun observeViewModel() {
        viewModel.operationResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Tidak melakukan apa-apa karena tidak ada ProgressBar
                        // Hanya menonaktifkan tombol submit
                        binding.btnSubmit.isEnabled = false
                        Toast.makeText(this, "Memproses...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        binding.btnSubmit.isEnabled = true
                        Toast.makeText(this, result.data, Toast.LENGTH_LONG).show()

                        // Kembali ke daftar layanan setelah sukses
                        val intent = Intent(this, ServiceListActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        binding.btnSubmit.isEnabled = true
                        val errorMessage = result.message ?: "Terjadi kesalahan."
                        Toast.makeText(this, "Gagal: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val fileName = "upload_${System.currentTimeMillis()}"
            val tempFile = File(cacheDir, fileName)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Log.d("AddServiceActivity", "Temp file created: ${tempFile.absolutePath}, exists: ${tempFile.exists()}, size: ${tempFile.length()}")
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AddServiceActivity", "Error creating temp file from URI: ${e.message}")
            null
        }
    }

    companion object {
        const val EXTRA_SERVICE = "extra_service"
    }
}