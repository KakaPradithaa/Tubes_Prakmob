package com.example.bengkelappclient.ui.theme.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.remote.ApiClient
import com.example.bengkelappclient.databinding.ActivityEditProfileBinding
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        setupToolbar()
        setupBottomNavButtons()
        getUserProfile()

        binding.btnSave.setOnClickListener {
            updateProfile()
        }

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Profil"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBottomNavButtons() {
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }
        binding.navProfile.setOnClickListener { /* Do nothing */ }
        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
        }
    }

    private fun getUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.getUserProfile()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            // Tampilkan data di UI
                            userId = user.id
                            binding.etNama.setText(user.name)
                            binding.etEmail.setText(user.email)
                            binding.etPhone.setText(user.phone)
                            binding.etAddress.setText(user.address)

                            // --- PERBAIKAN DI SINI ---
                            // Simpan kembali data terbaru ke SharedPreferences
                            with(sharedPref.edit()) {
                                putString("USERNAME", user.name)
                                // Anda juga bisa menyimpan data lain jika diperlukan
                                apply()
                            }
                        } else {
                            Toast.makeText(this@EditProfileActivity, "Respons data pengguna kosong.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Gagal memuat profil. Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateProfile() {
        // Ambil data dari input fields
        val nama = binding.etNama.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val oldPassword = binding.etOldPassword.text.toString()
        val newPassword = binding.etPassword.text.toString()

        // Validasi input dasar
        if (nama.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Nama, telepon, dan alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // Siapkan data untuk dikirim ke API
        val updateData = mutableMapOf(
            "name" to nama,
            "phone" to phone,
            "address" to address
        )

        // Logika untuk perubahan password
        if (newPassword.isNotEmpty()) {
            if (oldPassword.isEmpty()) {
                Toast.makeText(this, "Masukkan password lama untuk mengubah password", Toast.LENGTH_SHORT).show()
                return
            }
            updateData["old_password"] = oldPassword
            updateData["password"] = newPassword
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // **PERBAIKAN DI SINI:** Panggil API hanya dengan `updateData`
                val response = ApiClient.apiService.updateUserProfile(updateData)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfileActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        // Muat ulang data profil. Fungsi ini sekarang juga akan menyimpan ulang ke SharedPreferences.
                        getUserProfile()
                    } else {
                        // LOGIKA BARU: Tampilkan pesan error dari server
                        val errorBody = response.errorBody()?.string()
                        var errorMessage = "Gagal memperbarui profil. Error: ${response.code()}"
                        if (errorBody != null) {
                            try {
                                val jsonObject = JSONObject(errorBody)
                                // Ambil pesan error utama jika ada
                                if(jsonObject.has("message")) {
                                    errorMessage = jsonObject.getString("message")
                                }
                            } catch (e: Exception) {
                                // Gagal parse JSON, tampilkan error body mentah
                                errorMessage = errorBody
                            }
                        }
                        Toast.makeText(this@EditProfileActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun logoutUser() {
        // Hapus data dari SharedPreferences
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        // Arahkan ke LoginActivity dan hapus semua activity sebelumnya
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
