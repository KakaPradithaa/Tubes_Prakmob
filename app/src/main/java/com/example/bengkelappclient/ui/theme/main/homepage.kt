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

class homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
    }
}