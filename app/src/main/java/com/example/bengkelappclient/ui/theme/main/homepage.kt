// ui/theme/main/homepage.kt
package com.example.bengkelappclient.ui.theme.main

import android.content.Context // Import ini
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity

class homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Temukan TextView untuk nama pengguna
        val usernameTextView = findViewById<TextView>(R.id.textView4)

        // --- AMBIL NAMA PENGGUNA DARI SHARED PREFERENCES ---
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPref.getString("USERNAME", "Pengguna") // "Pengguna" adalah nilai default jika tidak ditemukan

        // Atur nama pengguna ke TextView
        username?.let {
            usernameTextView.text = it
        }

        // Tombol untuk Booking Service
        val btnReservasi = findViewById<Button>(R.id.btnReservasi)
        btnReservasi.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // Tombol untuk ke halaman Edit Profil
        val profileButton = findViewById<ImageButton>(R.id.nav_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Tombol untuk ke halaman Order Status
        val historyButton = findViewById<ImageButton>(R.id.nav_history)
        historyButton.setOnClickListener {
            val intent = Intent(this, OrderStatusActivity::class.java)
            startActivity(intent)
        }
    }
}