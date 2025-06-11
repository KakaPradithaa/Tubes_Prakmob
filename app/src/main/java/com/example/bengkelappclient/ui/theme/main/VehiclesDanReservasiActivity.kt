package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.ImageButton
import android.widget.Toast
import com.example.bengkelappclient.R


class VehiclesDanReservasiActivity : AppCompatActivity() {

    private lateinit var btnAddVehicles: Button
    private lateinit var btnReservasi: Button
    private lateinit var backButton: ImageButton
    private lateinit var navHistory: ImageButton
    private lateinit var navProfile: ImageButton
    private lateinit var fabHome: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiclesdanreservasi)

        // Inisialisasi view
        btnAddVehicles = findViewById(R.id.btnaddVehicles)
        btnReservasi = findViewById(R.id.btnReservasi)
        backButton = findViewById(R.id.back_icon)
        navHistory = findViewById(R.id.nav_history)
        navProfile = findViewById(R.id.nav_profile)
        fabHome = findViewById(R.id.fab_home)

        // Aksi tombol Tambah Kendaraan
        btnAddVehicles.setOnClickListener {
            val intent = Intent(this, VehicleDataActivity::class.java)
            startActivity(intent)
        }

        // Aksi tombol Reservasi
        btnReservasi.setOnClickListener {
            val intent = Intent(this, BookingServiceActivity::class.java)
            startActivity(intent)
        }

        // Aksi tombol kembali
        backButton.setOnClickListener {
            finish()
        }

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
