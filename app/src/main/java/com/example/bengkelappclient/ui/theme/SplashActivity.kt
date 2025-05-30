package com.example.bengkelappclient.ui.splash // Pastikan package ini benar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bengkelappclient.databinding.ActivitySplashBinding
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.main.MainActivity // <<--- IMPORT YANG BENAR
import com.example.bengkelappclient.ui.theme.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(1500)

            mainViewModel.isUserLoggedIn.collect { isLoggedIn ->
                Log.d("SplashActivity", "isUserLoggedIn collected: $isLoggedIn")
                when (isLoggedIn) {
                    true -> navigateToMain()
                    false -> navigateToLogin()
                    null -> {
                        Log.d("SplashActivity", "Login status is null, waiting...")
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        Log.d("SplashActivity", "Navigating to MainActivity")
        val intent = Intent(this, MainActivity::class.java) // Menggunakan MainActivity yang diimpor dengan benar
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        Log.d("SplashActivity", "Navigating to LoginActivity")
        // Pastikan LoginActivity juga diimpor dari package yang benar jika Anda memindahkannya
        // import com.example.bengkelappclient.ui.auth.LoginActivity;
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}