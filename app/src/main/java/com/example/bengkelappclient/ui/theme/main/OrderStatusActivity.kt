package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R

class OrderStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        // ikon panah untuk ke halaman homepage
        val backButton = findViewById<ImageButton>(R.id.back_icon)
        backButton.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        // Tombol untuk ke halaman homepage
        val homeButton = findViewById<ImageButton>(R.id.fab_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }

        // Tombol untuk ke halaman Edit Profil
        val profileButton = findViewById<ImageButton>(R.id.nav_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}