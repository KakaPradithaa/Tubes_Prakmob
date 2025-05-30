// ui/auth/LoginActivity.kt
package com.example.bengkelappclient.ui.theme.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.databinding.ActivityLoginBinding
import com.example.bengkelappclient.ui.auth.AuthViewModel
import com.example.bengkelappclient.ui.auth.RegisterActivity
import com.example.bengkelappclient.ui.theme.main.MainActivity
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            // finish() // Opsional, tergantung alur navigasi yang diinginkan
        }
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBarLogin.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = false
                    }
                    is Resource.Success -> {
                        binding.progressBarLogin.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        // Navigasi ke MainActivity (Dashboard)
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity() // Hapus semua activity sebelumnya dari back stack
                    }
                    is Resource.Error -> {
                        binding.progressBarLogin.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this, resource.message ?: "Login Gagal", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}