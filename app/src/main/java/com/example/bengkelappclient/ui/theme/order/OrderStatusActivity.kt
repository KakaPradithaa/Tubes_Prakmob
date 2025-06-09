package com.example.bengkelappclient.ui.theme.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.ui.adapter.OrderStatusAdapter
import com.example.bengkelappclient.ui.theme.main.EditProfileActivity
import com.example.bengkelappclient.ui.theme.main.homepage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStatusActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var orderStatusAdapter: OrderStatusAdapter // Pastikan ini dideklarasikan

    // Menggunakan lazy initialization untuk ViewModel karena by viewModels() harus dilakukan di dalam Activity
    private val viewModel: OrderStatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        // Inisialisasi View
        recyclerView = findViewById(R.id.recyclerViewBookingHistory)
        progressBar = findViewById(R.id.progressBar)

        // Inisialisasi adapter SEBELUM diatur ke RecyclerView
        orderStatusAdapter = OrderStatusAdapter()

        // Setup RecyclerView setelah adapter diinisialisasi
        setupRecyclerView()

        // Mulai observasi ViewModel
        observeViewModel()

        // Panggil pengambilan data setelah setup
        viewModel.fetchBookingHistory()
        Log.d("OrderStatusActivity", "Memulai pengambilan riwayat pemesanan.")

        // Memanggil fungsi untuk inisialisasi fitur tambahan
        initAdditionalFeatures()

        // Handle back button
        val backButton = findViewById<ImageButton>(R.id.back_icon)
        backButton.setOnClickListener {
            finish()
        }

        // Handle bottom navigation buttons
        findViewById<ImageButton>(R.id.fab_home).setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderStatusActivity)
            adapter = orderStatusAdapter // Pastikan adapter sudah diinisialisasi saat ini
        }
    }

    private fun observeViewModel() {
        viewModel.bookingHistory.observe(this) { result ->
            when (result) {
                is ServiceResult.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    Log.d("OrderStatusActivity", "Status: Loading data...")
                }
                is ServiceResult.Success -> {
                    progressBar.visibility = View.GONE
                    if (result.data.isNullOrEmpty()) {
                        Toast.makeText(this, "Tidak ada riwayat pesanan.", Toast.LENGTH_SHORT).show()
                        Log.d("OrderStatusActivity", "Daftar pemesanan kosong.")
                        orderStatusAdapter.submitList(emptyList()) // Pastikan adapter menerima daftar kosong jika tidak ada data
                    } else {
                        orderStatusAdapter.submitList(result.data)
                        Log.d("OrderStatusActivity", "Status: Data berhasil dimuat. Jumlah item: ${result.data.size}")
                    }
                }
                is ServiceResult.Error -> {
                    progressBar.visibility = View.GONE
                    val errorMessage = result.message ?: "Terjadi kesalahan tidak diketahui."
                    Toast.makeText(
                        this,
                        "Gagal memuat riwayat pesanan: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("OrderStatusActivity", "Status: Error memuat data: $errorMessage", result.exception)
                    orderStatusAdapter.submitList(emptyList()) // Kosongkan daftar jika ada error
                }
            }
        }
    }

    /**
     * Fungsi ini adalah placeholder untuk fitur tambahan yang mungkin ingin Anda tambahkan di masa mendatang.
     * Panggil fungsi ini dari onCreate() untuk menginisialisasi fitur-fitur tersebut.
     */
    private fun initAdditionalFeatures() {
        // Anda bisa menambahkan inisialisasi komponen UI tambahan di sini,
        // atau logika bisnis lain yang tidak terkait langsung dengan pemuatan riwayat pesanan utama.
        Log.d("OrderStatusActivity", "Fungsi initAdditionalFeatures() dipanggil.")
    }
}
