package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.databinding.ActivityHomepageBinding
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class homepage : AppCompatActivity() {

    // Menggunakan ViewBinding untuk akses UI yang lebih aman
    private lateinit var binding: ActivityHomepageBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi ViewBinding
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Selalu muat ulang data (terutama nama pengguna) saat halaman kembali aktif
        homeViewModel.refreshData()
    }

    private fun setupButtons() {
        // Menggunakan binding untuk listener tombol
        binding.btnReservasi.setOnClickListener {
            // Mengambil intent dari versi kedua (lebih spesifik)
            startActivity(Intent(this, VehiclesDanReservasiActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }
        binding.fabHome.setOnClickListener {
            // Tombol home, biasanya tidak ada aksi jika sudah di halaman home
        }
    }

    private fun observeViewModel() {
        // Mengamati perubahan nama pengguna
        homeViewModel.userName.observe(this) { name ->
            // Menggunakan binding untuk mengakses TextView dengan ID yang benar dari XML Anda
            binding.textView4.text = name ?: "Pengguna"
        }

        // Mengamati perubahan daftar layanan
        homeViewModel.services.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Homepage", "Memuat layanan...")
                    // Anda bisa menampilkan ProgressBar di sini
                }
                is Resource.Success -> {
                    val services = result.data
                    if (!services.isNullOrEmpty()) {
                        displayServicesInGrid(services)
                    } else {
                        Toast.makeText(this, "Tidak ada layanan yang tersedia.", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    val errorMessage = result.message ?: "Gagal memuat layanan."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayServicesInGrid(services: List<Service>) {
        // Menggunakan binding untuk mengakses GridLayout
        binding.gridLayoutLayanan.removeAllViews()

        val inflater = LayoutInflater.from(this)
        val BASE_IMAGE_URL = "http://10.0.2.2:8000/uploads/services/"

        // Logika untuk menghitung lebar item
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val gridPadding = resources.getDimensionPixelSize(R.dimen.grid_padding) * 2
        val itemMargin = resources.getDimensionPixelSize(R.dimen.grid_item_margin)
        val totalMargin = itemMargin * (binding.gridLayoutLayanan.columnCount * 2)
        val itemWidth = (screenWidth - gridPadding - totalMargin) / binding.gridLayoutLayanan.columnCount
        val itemHeight = resources.getDimensionPixelSize(R.dimen.grid_item_height)

        for ((index, service) in services.withIndex()) {
            val serviceItemView = inflater.inflate(R.layout.homepage_grid_item_service, binding.gridLayoutLayanan, false)

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

            // Mengatur parameter layout untuk item
            val params = GridLayout.LayoutParams(serviceItemView.layoutParams)
            params.width = itemWidth
            params.height = itemHeight
            params.setMargins(itemMargin, itemMargin, itemMargin, itemMargin)
            serviceItemView.layoutParams = params

            serviceItemView.setOnClickListener {
                onServiceItemClick(service)
            }

            binding.gridLayoutLayanan.addView(serviceItemView)
        }
    }

    private fun onServiceItemClick(service: Service) {
        val intent = Intent(this, DetailLayananActivity::class.java).apply {
            putExtra("judul_layanan", service.name)
            putExtra("deskripsi_layanan", service.description)
            putExtra("image_layanan_url", "http://10.0.2.2:8000/uploads/services/" + service.img)
        }
        startActivity(intent)
    }
}
