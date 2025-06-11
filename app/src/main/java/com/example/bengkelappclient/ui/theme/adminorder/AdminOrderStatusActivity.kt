package com.example.bengkelappclient.ui.theme.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.ui.adapter.AdminOrderStatusAdapter // Pastikan ini diimport
import com.example.bengkelappclient.ui.theme.order.OrderStatusViewModel
import com.example.bengkelappclient.util.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminOrderStatusActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyMessage: TextView
    private lateinit var adminOrderStatusAdapter: AdminOrderStatusAdapter

    private val orderStatusViewModel: OrderStatusViewModel by viewModels()
    private val adminOrderViewModel: AdminOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order_status)

        recyclerView = findViewById(R.id.recyclerViewBookingHistory)
        progressBar = findViewById(R.id.progressBar)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

        // PASTIKAN INISIALISASI ADAPTER SEPERTI INI
        adminOrderStatusAdapter = AdminOrderStatusAdapter { bookingDetails -> // <--- INI PENTING
            showUpdateStatusDialog(bookingDetails) // <--- DAN INI MEMANGGIL FUNGSI SHOW DIALOG
        }

        setupRecyclerView()
        observeViewModels()

        orderStatusViewModel.fetchBookingHistory()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminOrderStatusActivity)
            adapter = adminOrderStatusAdapter
        }
    }

    private fun observeViewModels() {
        orderStatusViewModel.bookingHistory.observe(this) { result ->
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
                        tvEmptyMessage.text = "Belum ada pesanan untuk dikelola."
                        tvEmptyMessage.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        tvEmptyMessage.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        adminOrderStatusAdapter.submitList(bookingList)
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    tvEmptyMessage.text = result.message ?: "Terjadi kesalahan"
                    tvEmptyMessage.visibility = View.VISIBLE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        adminOrderViewModel.updateStatusResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Toast.makeText(this, "Memperbarui status...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(this, "Status berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        orderStatusViewModel.fetchBookingHistory()
                    }
                    is Resource.Error -> {
                        Toast.makeText(this, resource.message ?: "Gagal memperbarui status", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        findViewById<ImageButton>(R.id.back_icon).setOnClickListener {
            finish()
        }
    }

    private fun showUpdateStatusDialog(bookingDetails: BookingDetails) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_update_order_status, null)
        dialog.setContentView(view)

        // Inisialisasi Views dari dialog
        val statusPendingCard: MaterialCardView = view.findViewById(R.id.status_pending)
        val statusConfirmedCard: MaterialCardView = view.findViewById(R.id.status_confirmed)
        val statusInProgressCard: MaterialCardView = view.findViewById(R.id.status_in_progress)
        val statusCompletedCard: MaterialCardView = view.findViewById(R.id.status_completed)
        val statusCancelledCard: MaterialCardView = view.findViewById(R.id.status_cancelled)

        val iconPending: ImageView = view.findViewById(R.id.icon_pending_selection)
        val iconConfirmed: ImageView = view.findViewById(R.id.icon_confirmed_selection)
        val iconInProgress: ImageView = view.findViewById(R.id.icon_in_progress_selection)
        val iconCompleted: ImageView = view.findViewById(R.id.icon_completed_selection)
        val iconCancelled: ImageView = view.findViewById(R.id.icon_cancelled_selection)

        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val btnUpdateStatus: Button = view.findViewById(R.id.btn_update_status)

        var selectedStatus: String = bookingDetails.booking.status.lowercase()

        // Fungsi untuk mengupdate tampilan ikon berdasarkan status yang dipilih (HANYA MENGUBAH TINT)
        fun updateSelectionIcons() {
            val selectedColor = ContextCompat.getColor(this, R.color.indigoblue)
            val defaultColor = ContextCompat.getColor(this, R.color.grey)

            // Hanya mengatur tint warna ikon, gambar ikon tetap yang dari XML
            iconPending.setColorFilter(if (selectedStatus == "pending") selectedColor else defaultColor)
            iconConfirmed.setColorFilter(if (selectedStatus == "confirmed") selectedColor else defaultColor)
            iconInProgress.setColorFilter(if (selectedStatus == "in_progress") selectedColor else defaultColor)
            iconCompleted.setColorFilter(if (selectedStatus == "completed") selectedColor else defaultColor)
            iconCancelled.setColorFilter(if (selectedStatus == "cancelled") selectedColor else defaultColor)
        }

        // Set status awal
        updateSelectionIcons()

        // Set Listener untuk setiap kartu status
        statusPendingCard.setOnClickListener {
            selectedStatus = "pending"
            updateSelectionIcons()
        }
        statusConfirmedCard.setOnClickListener {
            selectedStatus = "confirmed"
            updateSelectionIcons()
        }
        statusInProgressCard.setOnClickListener {
            selectedStatus = "in_progress"
            updateSelectionIcons()
        }
        statusCompletedCard.setOnClickListener {
            selectedStatus = "completed"
            updateSelectionIcons()
        }
        statusCancelledCard.setOnClickListener {
            selectedStatus = "cancelled"
            updateSelectionIcons()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdateStatus.setOnClickListener {
            adminOrderViewModel.updateBookingStatus(bookingDetails.booking.id, selectedStatus)
            dialog.dismiss()
        }

        dialog.show()
    }
}