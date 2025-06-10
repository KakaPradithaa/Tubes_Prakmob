package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bengkelappclient.R
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import com.example.bengkelappclient.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Inisialisasi input
        etNama = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)

        // Data awal dummy
        etNama.setText("admin")
        etEmail.setText("admin@example.com")
        etPhone.setText("085789070987")
        etAddress.setText("Jl. Setiabudi No. 10")

        btnSave.setOnClickListener {
            val nama = etNama.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val phone = etPhone.text.toString()
            val address = etAddress.text.toString()

            if (nama.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupBottomNavButtons()
    }

    private fun setupBottomNavButtons() {
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
        }
    }
}
