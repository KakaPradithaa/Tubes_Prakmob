// ui/main/MainActivity.kt
package com.example.bengkelappclient

import android.content.Intent
import android.os.Bundle
import android.util.Log // Tambahkan Log untuk debugging jika perlu
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bengkelappclient.R
import com.example.bengkelappclient.databinding.ActivityMainBinding
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.customer.CustomerListFragment
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
                Log.d("MainActivity", "isUserLoggedIn collected: $isLoggedIn") // Log untuk debug
                when (isLoggedIn) { // <<--- GUNAKAN WHEN UNTUK MENANGANI NULLABLE BOOLEAN
                    true -> {
                        // User sudah login, setup dashboard
                        Log.d("MainActivity", "User is logged in, setting up dashboard.")
                        setupDashboard()
                    }
                    false -> {
                        // User belum login, navigasi ke LoginActivity
                        Log.d("MainActivity", "User is NOT logged in, navigating to LoginActivity.")
                        navigateToLoginAfterLogout() // Menggunakan fungsi yang sama untuk redirect
                    }
                    null -> {
                        // Status login masih belum diketahui (initialValue atau sedang loading dari DataStore)
                        // Di MainActivity, ini berarti SplashActivity mungkin belum selesai
                        // atau terjadi kondisi di mana state kembali null.
                        // Biasanya, jika SplashActivity sudah benar, MainActivity hanya akan
                        // dimulai jika isLoggedIn sudah true.
                        // Namun, untuk menangani logout saat MainActivity aktif, kita tetap perlu observasi.
                        // Jika null muncul saat MainActivity sudah aktif, itu bisa jadi indikasi
                        // ada masalah dengan state management, atau kita bisa anggap sebagai "loading" / "indeterminate".
                        Log.d("MainActivity", "Login status is null (indeterminate/loading).")
                        // Anda bisa menampilkan ProgressBar di MainActivity jika ini terjadi,
                        // atau jika yakin SplashActivity sudah handle, bagian ini mungkin tidak akan sering tercapai
                        // kecuali setelah logout dan sebelum redirect.
                    }
                }
            }
        }
    }

    // Fungsi ini dipanggil jika user logout ATAU jika saat pertama kali MainActivity
    // (setelah Splash) ternyata user tidak login (meskipun Splash seharusnya sudah handle ini)
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

        binding.btnViewCustomers.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CustomerListFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                // Navigasi akan ditangani oleh kolektor isUserLoggedIn di atas
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