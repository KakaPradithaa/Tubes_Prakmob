package com.example.bengkelappclient.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bengkelappclient.data.model.Schedule
import com.example.bengkelappclient.databinding.ActivityScheduleBinding
import com.example.bengkelappclient.databinding.DialogEditScheduleBinding
import com.example.bengkelappclient.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        setupRecyclerView()
        observeSchedules()
        observeUpdateResult()

        viewModel.getSchedules()
    }

    private fun setupRecyclerView() {
        scheduleAdapter = ScheduleAdapter { schedule ->
            showEditDialog(schedule)
        }
        binding.rvSchedules.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
        }
    }

    private fun observeSchedules() {
        viewModel.schedules.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> binding.loadingOverlay.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loadingOverlay.visibility = View.GONE
                    resource.data?.let {
                        if (it.isEmpty()) {
                            binding.emptyStateLayout.visibility = View.VISIBLE
                            binding.rvSchedules.visibility = View.GONE
                        } else {
                            binding.emptyStateLayout.visibility = View.GONE
                            binding.rvSchedules.visibility = View.VISIBLE
                            scheduleAdapter.submitList(it)
                        }
                    }
                }
                is Resource.Error -> {
                    binding.loadingOverlay.visibility = View.GONE
                    binding.emptyStateLayout.visibility = View.VISIBLE
                    binding.rvSchedules.visibility = View.GONE
                    Snackbar.make(binding.root, resource.message ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeUpdateResult() {
        viewModel.updateResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> { /* Optionally show a loading indicator */ }
                is Resource.Success -> {
                    Snackbar.make(binding.root, "Jadwal berhasil diperbarui", Snackbar.LENGTH_SHORT).show()
                    viewModel.getSchedules() // Refresh the list after update
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, resource.message ?: "Gagal memperbarui jadwal", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showEditDialog(schedule: Schedule) {
        val dialogBinding = DialogEditScheduleBinding.inflate(LayoutInflater.from(this))

        // Mengisi data yang ada ke dalam input fields
        // [FIXED] Mengubah referensi dari tvDialogDay menjadi tvDialogTitle
        dialogBinding.tvDialogTitle.text = "Ubah Jadwal untuk ${schedule.day}"
        // [FIXED] Mengubah referensi dari etOpenTime dan etCloseTime
        dialogBinding.etOpenTime.setText(schedule.openTime)
        dialogBinding.etCloseTime.setText(schedule.closeTime)
        // [FIXED] Mengubah referensi dari etSlots menjadi etMaxBooking
        dialogBinding.etMaxBooking.setText(schedule.slots.toString())

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedSchedule = schedule.copy(
                    openTime = dialogBinding.etOpenTime.text.toString(),
                    closeTime = dialogBinding.etCloseTime.text.toString(),
                    // [FIXED] Mengubah referensi dari etSlots menjadi etMaxBooking
                    slots = dialogBinding.etMaxBooking.text.toString().toIntOrNull() ?: schedule.slots
                )
                viewModel.updateSchedule(updatedSchedule)
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    private fun setupActionListeners() {
        // Menambahkan fungsi untuk tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}