package com.example.bengkelappclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bengkelappclient.databinding.ActivityMainBinding
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.main.MainViewModel
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

        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            mainViewModel.isUserLoggedIn.collect { isLoggedIn ->
                Log.d("MainActivity", "isUserLoggedIn collected: $isLoggedIn")
                when (isLoggedIn) {
                    true -> {
                        Log.d("MainActivity", "User is logged in, setting up dashboard.")
                        setupDashboard()
                    }
                    false -> {
                        Log.d("MainActivity", "User is NOT logged in, navigating to LoginActivity.")
                        navigateToLoginAfterLogout()
                    }
                    null -> {
                        Log.d("MainActivity", "Login status is null (indeterminate/loading).")
                        // Bisa tampilkan loading jika ingin
                    }
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

        // Jika kamu ingin tambah fitur seperti "Booking", bisa aktifkan ini:
        // binding.btnViewBookings.setOnClickListener {
        //     supportFragmentManager.beginTransaction()
        //         .replace(R.id.fragmentContainer, BookingListFragment())
        //         .addToBackStack(null)
        //         .commit()
        // }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                true
            }
            R.id.action_settings -> {
                // TODO: Tambahkan logika Settings jika diperlukan
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
