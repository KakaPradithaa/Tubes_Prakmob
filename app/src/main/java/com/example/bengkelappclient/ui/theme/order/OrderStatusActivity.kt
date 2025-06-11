package com.example.bengkelappclient.ui.theme.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bengkelappclient.R
import com.example.bengkelappclient.ui.adapter.OrderStatusAdapter
import com.example.bengkelappclient.ui.theme.main.EditProfileActivity
import com.example.bengkelappclient.ui.theme.main.homepage
import com.example.bengkelappclient.util.Resource // DIUBAH: Pastikan import Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStatusActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyMessage: TextView // Tambahkan untuk pesan kosong
    private lateinit var orderStatusAdapter: OrderStatusAdapter

    private val viewModel: OrderStatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        // Inisialisasi Views
        recyclerView = findViewById(R.id.recyclerViewBookingHistory)
        progressBar = findViewById(R.id.progressBar)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage) // Inisialisasi TextView untuk pesan kosong

        orderStatusAdapter = OrderStatusAdapter()

        setupRecyclerView()
        observeViewModel()

        // Panggil pengambilan data setelah setup
        viewModel.fetchBookingHistory()

        setupToolbar()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrderStatusActivity)
            adapter = orderStatusAdapter
        }
    }

    // DIUBAH: Fungsi ini disesuaikan untuk menangani Resource<T>
    private fun observeViewModel() {
        viewModel.bookingHistory.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    tvEmptyMessage.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    val bookingList = result.data
                    if (bookingList.isNullOrEmpty()) {
                        // Tampilkan pesan jika tidak ada data
                        tvEmptyMessage.text = "Anda belum memiliki riwayat pesanan."
                        tvEmptyMessage.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        // Tampilkan data ke RecyclerView
                        tvEmptyMessage.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        orderStatusAdapter.submitList(bookingList)
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    // Tampilkan pesan error
                    tvEmptyMessage.text = result.message ?: "Terjadi kesalahan"
                    tvEmptyMessage.visibility = View.VISIBLE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupToolbar() {
        findViewById<ImageButton>(R.id.back_icon).setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }
        // Navigasi bawah bisa dihapus dari sini jika ini bukan halaman utama
    }
}