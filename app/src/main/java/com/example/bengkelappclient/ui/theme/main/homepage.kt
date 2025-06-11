package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
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

    private lateinit var binding: ActivityHomepageBinding
    private val homeViewModel: HomeViewModel by viewModels()

    // --- PERBAIKAN UTAMA DAN SATU-SATUNYA ---
    // URL ini HARUS menunjuk ke folder 'storage' publik Anda, bukan langsung ke 'uploads'.
    private val BASE_URL = "http://10.0.2.2:8000/storage/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.refreshData()
    }

    private fun setupButtons() {
        binding.btnReservasi.setOnClickListener {
            startActivity(Intent(this, VehiclesDanReservasiActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }
        binding.fabHome.setOnClickListener {
            // Tidak ada aksi di halaman home
        }
    }

    private fun observeViewModel() {
        homeViewModel.userName.observe(this) { name ->
            binding.textView4.text = name ?: "Pengguna"
        }

        homeViewModel.services.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.d("Homepage", "Memuat layanan...")
                }
                is Resource.Success -> {
                    val services = result.data
                    if (!services.isNullOrEmpty()) {
                        displayServicesInGrid(services)
                    } else {
                        binding.gridLayoutLayanan.removeAllViews() // Kosongkan grid jika tidak ada data
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
        binding.gridLayoutLayanan.removeAllViews()
        val inflater = LayoutInflater.from(this)

        // ... (logika perhitungan layout Anda sudah oke, biarkan saja)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val gridPaddingLeft = binding.gridLayoutLayanan.paddingLeft
        val gridPaddingRight = binding.gridLayoutLayanan.paddingRight
        val totalGridPadding = gridPaddingLeft + gridPaddingRight
        val itemMargin = resources.getDimensionPixelSize(R.dimen.grid_item_margin)
        val columnCount = binding.gridLayoutLayanan.columnCount
        val availableWidth = screenWidth - totalGridPadding
        val totalMarginWidth = itemMargin * 2 * columnCount
        val itemWidth = (availableWidth - totalMarginWidth) / columnCount
        val itemHeight = itemWidth // Persegi

        for (service in services) {
            val serviceItemView = inflater.inflate(R.layout.homepage_grid_item_service, binding.gridLayoutLayanan, false)

            val serviceNameTextView = serviceItemView.findViewById<TextView>(R.id.tvServiceName)
            val serviceImageView = serviceItemView.findViewById<ImageView>(R.id.ivServiceImage)

            serviceNameTextView.text = service.name

            // Sekarang, URL akan dibangun dengan BENAR:
            // "http://10.0.2.2:8000/storage/" + "uploads/services/namafile.jpg"
            val imagePath = service.img ?: ""
            val imageUrl = BASE_URL + imagePath

            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_solid) // Ganti ke placeholder yang konsisten
                .error(R.drawable.placeholder_solid)
                .centerCrop()
                .into(serviceImageView)

            // ... (sisa kode untuk layout params dan click listener Anda sudah oke)
            val params = GridLayout.LayoutParams().apply {
                width = itemWidth
                height = itemHeight
                setMargins(itemMargin, itemMargin, itemMargin, itemMargin)
            }
            serviceItemView.layoutParams = params
            serviceItemView.setOnClickListener {
                onServiceItemClick(service)
            }

            binding.gridLayoutLayanan.addView(serviceItemView)
        }
    }

    private fun onServiceItemClick(service: Service) {
        // Di sini juga, URL dibangun dengan BENAR untuk dikirim ke Detail Activity
        val fullImageUrl = if (!service.img.isNullOrEmpty()) BASE_URL + service.img else ""

        val intent = Intent(this, DetailLayananActivity::class.java).apply {
            putExtra("judul_layanan", service.name)
            putExtra("deskripsi_layanan", service.description)
            putExtra("image_layanan_url", fullImageUrl)
        }
        startActivity(intent)
    }
}