package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.ui.theme.main.BookingActivity
import com.example.bengkelappclient.ui.theme.main.EditProfileActivity
import com.example.bengkelappclient.ui.theme.main.homepage
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailLayananActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageButton
    private lateinit var btnReservasi: Button
    private lateinit var navHistory: ImageButton
    private lateinit var navProfile: ImageButton
    private lateinit var fabHome: FloatingActionButton

    private lateinit var judulLayanan: TextView
    private lateinit var deskripsiLayanan: TextView
    private lateinit var imageLayanan: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_layanan)

        // Inisialisasi UI
        backIcon = findViewById(R.id.back_icon)
        btnReservasi = findViewById(R.id.btnReservasi)
        navHistory = findViewById(R.id.nav_history)
        navProfile = findViewById(R.id.nav_profile)
        fabHome = findViewById(R.id.fab_home)

        judulLayanan = findViewById(R.id.judulLayanan)
        deskripsiLayanan = findViewById(R.id.deskripsiLayanan)
        imageLayanan = findViewById(R.id.imageLayanan)

        // Ambil data dari Intent
        val judul = intent.getStringExtra("judul_layanan")
        val deskripsi = intent.getStringExtra("deskripsi_layanan")
        val imageUrl = intent.getStringExtra("image_layanan_url")

        judulLayanan.text = judul ?: "Judul Layanan"
        deskripsiLayanan.text = deskripsi ?: "Deskripsi tidak tersedia."

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.baseline_hide_image_24)
            .centerCrop()
            .into(imageLayanan)

        // Tombol kembali
        backIcon.setOnClickListener {
            finish()
        }

        // Tombol reservasi
        btnReservasi.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        // Bottom bar buttons
        navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        fabHome.setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
        }
    }
}

