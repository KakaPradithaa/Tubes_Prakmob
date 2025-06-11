// ui/auth/RegisterActivity.kt
package com.example.bengkelappclient.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.databinding.ActivityRegisterBinding
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString().trim()
            val passwordConfirm = binding.etPasswordConfirm.text.toString().trim()
            // Get phone and address from their respective EditText fields (you'll add these in the XML)
            val phone = binding.etPhoneRegister.text.toString().trim() // Assuming etPhoneRegister is the ID
            val address = binding.etAddressRegister.text.toString().trim() // Assuming etAddressRegister is the ID

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) { // Add checks for phone and address
                if (password == passwordConfirm) {
                    // Pass phone and address to the ViewModel
                    viewModel.register(name, email, password, passwordConfirm, phone, address)
                } else {
                    Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {
        viewModel.registerResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBarRegister.visibility = View.VISIBLE
                        binding.btnRegister.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.progressBarRegister.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(this, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
                        // Navigasi ke Login atau langsung ke MainActivity jika API mengembalikan token saat register
                        // Jika API register juga me-login user dan return token:
                        // startActivity(Intent(this, MainActivity::class.java))
                        // finishAffinity()

                        // Jika API register hanya mendaftar, arahkan ke login:
                        finish() // Kembali ke LoginActivity
                    }
                    is Resource.Error -> {
                        binding.progressBarRegister.visibility = View.GONE
                        binding.btnRegister.isEnabled = true
                        Toast.makeText(this, resource.message ?: "Registrasi Gagal", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}