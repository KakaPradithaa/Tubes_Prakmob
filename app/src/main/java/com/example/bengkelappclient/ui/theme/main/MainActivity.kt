// ui/main/MainActivity.kt
package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Pastikan import ini ada
import com.example.bengkelappclient.R
import com.example.bengkelappclient.databinding.ActivityMainBinding
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // Asumsi ID Toolbar adalah "toolbar"

        // Logika pengecekan login dipindahkan ke SplashActivity
        // Langsung setup dashboard karena Activity ini hanya akan dimulai jika sudah login
        setupDashboard()

        // Observer untuk logout (jika token menjadi tidak valid saat app berjalan)
        lifecycleScope.launch {
            mainViewModel.isUserLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn == false) { // Jika menjadi false saat activity ini aktif
                    navigateToLoginAfterLogout()
                }
            }
        }
    }

    private fun navigateToLoginAfterLogout() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupDashboard() {
        lifecycleScope.launch {
            mainViewModel.userName.collect { name ->
                binding.tvWelcomeMessage.text = "Selamat Datang, ${name ?: "Pengguna"}!"
            }
        }
        binding.tvDashboardContent.text = "Ini adalah halaman Dashboard Bengkel App."
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                // Navigasi ke LoginActivity akan ditangani oleh observer isUserLoggedIn
                true
            }
            R.id.action_settings -> {
                // TODO: Logika untuk Settings
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}