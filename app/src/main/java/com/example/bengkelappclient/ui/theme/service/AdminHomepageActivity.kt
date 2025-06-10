package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.databinding.ActivityAdminHomepageBinding
import com.example.bengkelappclient.ui.service.AddServiceActivity
import com.example.bengkelappclient.ui.service.ServiceListActivity


class AdminHomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuButtons()
        setupDashboardStats()
    }

    private fun setupMenuButtons() {
        binding.btnAddService.setOnClickListener {
            startActivity(Intent(this, AddServiceActivity::class.java))
        }

        binding.btnServiceList.setOnClickListener {
            startActivity(Intent(this, ServiceListActivity::class.java))
        }

        binding.btnOrder.setOnClickListener {
            Toast.makeText(this, "Halaman Order belum tersedia", Toast.LENGTH_SHORT).show()
        }

        binding.btnWorkshopSchedule.setOnClickListener {
            Toast.makeText(this, "Halaman Jadwal Operasi Bengkel belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDashboardStats() {
        // Data Dummy
        val statsData = mapOf(
            "pending" to 5,
            "confirmed" to 8,
            "in_progress" to 3,
            "completed" to 25,
            "cancelled" to 2
        )

        // Set stats to views
        binding.tvPendingCount.text = (statsData["pending"] ?: 0).toString()
        binding.tvConfirmedCount.text = (statsData["confirmed"] ?: 0).toString()
        binding.tvInProgressCount.text = (statsData["in_progress"] ?: 0).toString()
        binding.tvCompletedCount.text = (statsData["completed"] ?: 0).toString()
        binding.tvCancelledCount.text = (statsData["cancelled"] ?: 0).toString()
    }
}