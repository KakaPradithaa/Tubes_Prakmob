package com.example.bengkelappclient.ui.theme.admin

import android.content.Intent
import android.os.Bundle
import android.view.View // Import View untuk ProgressBar/EmptyState
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.databinding.ActivityAdminHomepageBinding
import com.example.bengkelappclient.ui.schedule.ScheduleActivity
import com.example.bengkelappclient.ui.theme.auth.LoginActivity
import com.example.bengkelappclient.ui.theme.service.ServiceListActivity
import com.example.bengkelappclient.util.Resource // Pastikan import Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomepageBinding
    private val adminHomeViewModel: AdminHomeViewModel by viewModels() // Injeksi ViewModel baru

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuButtons()
        // Hapus pemanggilan setupDashboardStats() di sini karena akan digantikan oleh observer ViewModel
        observeViewModel() // Panggil observer ViewModel
    }

    private fun setupMenuButtons() {
        binding.btnAddService.setOnClickListener {
            startActivity(Intent(this, AddServiceActivity::class.java))
        }

        binding.btnServiceList.setOnClickListener {
            startActivity(Intent(this, ServiceListActivity::class.java))
        }

        binding.btnOrder.setOnClickListener {
            startActivity(Intent(this, AdminOrderStatusActivity::class.java))
        }

        binding.btnWorkshopSchedule.setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    // Ganti fungsi setupDashboardStats() dengan observer ViewModel
    private fun observeViewModel() {
        adminHomeViewModel.bookingCounts.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Opsional: tampilkan spinner atau indikator loading di area statistik
                    // binding.progressBarStats.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    // binding.progressBarStats.visibility = View.GONE
                    val counts = resource.data ?: emptyMap()
                    binding.tvPendingCount.text = (counts["pending"] ?: 0).toString()
                    binding.tvConfirmedCount.text = (counts["confirmed"] ?: 0).toString()
                    binding.tvInProgressCount.text = (counts["in_progress"] ?: 0).toString()
                    binding.tvCompletedCount.text = (counts["completed"] ?: 0).toString()
                    binding.tvCancelledCount.text = (counts["cancelled"] ?: 0).toString()
                }
                is Resource.Error -> {
                    // binding.progressBarStats.visibility = View.GONE
                    val errorMessage = resource.message ?: "Gagal memuat statistik."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    // Tampilkan 0 atau pesan error di TextView jika perlu
                    binding.tvPendingCount.text = "0"
                    binding.tvConfirmedCount.text = "0"
                    binding.tvInProgressCount.text = "0"
                    binding.tvCompletedCount.text = "0"
                    binding.tvCancelledCount.text = "0"
                }
            }
        }
    }

    // Hapus fungsi setupDashboardStats() yang lama (jika ada) karena sudah diganti.
    /*
    private fun setupDashboardStats() {
        // Data Dummy (akan dihapus)
        val statsData = mapOf(
            "pending" to 5,
            "confirmed" to 8,
            "in_progress" to 3,
            "completed" to 25,
            "cancelled" to 2
        )

        binding.tvPendingCount.text = (statsData["pending"] ?: 0).toString()
        binding.tvConfirmedCount.text = (statsData["confirmed"] ?: 0).toString()
        binding.tvInProgressCount.text = (statsData["in_progress"] ?: 0).toString()
        binding.tvCompletedCount.text = (statsData["completed"] ?: 0).toString()
        binding.tvCancelledCount.text = (statsData["cancelled"] ?: 0).toString()
    }
    */
}