    package com.example.bengkelappclient.ui.splash

    import android.annotation.SuppressLint
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import com.example.bengkelappclient.databinding.ActivitySplashBinding
    import com.example.bengkelappclient.ui.theme.auth.LoginActivity
    import com.example.bengkelappclient.ui.service.AddServiceActivity
    import com.example.bengkelappclient.ui.theme.main.MainActivity
    import com.example.bengkelappclient.ui.theme.main.MainViewModel
    import dagger.hilt.android.AndroidEntryPoint
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.first
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

                try {
                    val isLoggedIn = mainViewModel.isUserLoggedIn.first()
                    Log.d("SplashActivity", "isUserLoggedIn: $isLoggedIn")

                    if (isLoggedIn == true) {
                        navigateBasedOnRole()
                    } else {
                        navigateToLogin()
                    }

                } catch (e: Exception) {
                    Log.e("SplashActivity", "Error reading login status: ${e.message}")
                    navigateToLogin()
                }
            }
        }

        private fun navigateBasedOnRole() {
            val prefs = getSharedPreferences("user_pref", MODE_PRIVATE)
            val role = prefs.getString("role", "")

            Log.d("SplashActivity", "User role: $role")

            val intent = when (role?.lowercase()) {
                "admin" -> Intent(this, AddServiceActivity::class.java)
                "user" -> Intent(this, MainActivity::class.java)
                else -> Intent(this, LoginActivity::class.java) // fallback
            }

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        private fun navigateToLogin() {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
