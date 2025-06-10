// ui/theme/main/homepage.kt
package com.example.bengkelappclient.ui.theme.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class homepage : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var gridLayoutLayanan: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Temukan TextView untuk nama pengguna
        val usernameTextView = findViewById<TextView>(R.id.textView4)

        // Temukan GridLayout
        gridLayoutLayanan = findViewById(R.id.gridLayoutLayanan)

        // Setup GridLayout properties
        setupGridLayout()

        // --- AMBIL NAMA PENGGUNA DARI SHARED PREFERENCES ---
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPref.getString("USERNAME", "Pengguna")

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

        // Amati layanan dari ViewModel
        observeServices()
    }

    private fun setupGridLayout() {
        // Set 3 kolom untuk grid
        gridLayoutLayanan.columnCount = 3
        gridLayoutLayanan.rowCount = GridLayout.UNDEFINED // Biarkan row count otomatis

        // Set alignment untuk center
        gridLayoutLayanan.alignmentMode = GridLayout.ALIGN_BOUNDS
        gridLayoutLayanan.useDefaultMargins = false
    }

    private fun observeServices() {
        homeViewModel.services.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    Log.d("Homepage", "Memuat layanan...")
                }
                is ServiceResult.Success -> {
                    val services = result.data
                    if (!services.isNullOrEmpty()) {
                        displayServicesInGrid(services)
                        Log.d("Homepage", "Layanan berhasil dimuat: ${services.size} item")
                    } else {
                        Toast.makeText(this, "Tidak ada layanan tersedia.", Toast.LENGTH_SHORT).show()
                        Log.d("Homepage", "Daftar layanan kosong.")
                    }
                }
                is ServiceResult.Error -> {
                    val errorMessage = result.message ?: "Gagal memuat layanan."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("Homepage", "Error memuat layanan: $errorMessage", result.exception)
                }
            }
        }
    }

    private fun displayServicesInGrid(services: List<com.example.bengkelappclient.data.model.Service>) {
        gridLayoutLayanan.removeAllViews()

        val inflater = LayoutInflater.from(this)
        val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/"

        // Hitung lebar item berdasarkan lebar layar dengan padding GridLayout
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val gridPadding = resources.getDimensionPixelSize(R.dimen.grid_padding) * 2 // kiri + kanan
        val itemMargin = resources.getDimensionPixelSize(R.dimen.grid_item_margin)
        val totalMargin = itemMargin * 6 // 2 margin per item × 3 kolom
        val availableWidth = screenWidth - gridPadding - totalMargin
        val itemWidth = availableWidth / 3

        // Ambil tinggi item yang konsisten
        val itemHeight = resources.getDimensionPixelSize(R.dimen.grid_item_height)

        for ((index, service) in services.withIndex()) {
            val serviceItemView = inflater.inflate(R.layout.homepage_grid_item_service, gridLayoutLayanan, false) as LinearLayout

            val serviceNameTextView = serviceItemView.findViewById<TextView>(R.id.tvServiceName)
            val serviceImageView = serviceItemView.findViewById<ImageView>(R.id.ivServiceImage)

            serviceNameTextView.text = service.name
            val imageUrl = BASE_IMAGE_URL + service.img

            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.baseline_hide_image_24)
                .centerCrop()
                .into(serviceImageView)

            // Hitung posisi kolom dan baris
            val column = index % 3
            val row = index / 3

            // Atur parameter GridLayout dengan ukuran yang sama
            val params = GridLayout.LayoutParams()
            params.width = itemWidth
            params.height = itemHeight

            // Spesifikasi kolom dan baris tanpa weight untuk ukuran tetap
            params.columnSpec = GridLayout.spec(column, 1)
            params.rowSpec = GridLayout.spec(row, 1)

            // Set margin yang konsisten
            params.setMargins(itemMargin, itemMargin, itemMargin, itemMargin)

            serviceItemView.layoutParams = params

            // Tambahkan click listener jika diperlukan
            serviceItemView.setOnClickListener {
                onServiceItemClick(service)
            }

            gridLayoutLayanan.addView(serviceItemView)
        }
    }

    private fun onServiceItemClick(service: com.example.bengkelappclient.data.model.Service) {
        // Implementasi ketika service item diklik
        Toast.makeText(this, "Layanan: ${service.name}", Toast.LENGTH_SHORT).show()
        // Anda bisa menambahkan navigasi ke halaman detail service di sini
    }
}