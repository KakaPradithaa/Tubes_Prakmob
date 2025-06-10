package com.example.bengkelappclient.ui.theme.main

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.example.bengkelappclient.databinding.ActivityBookingBinding
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbarBooking)

        // Hilangkan judul default dan aktifkan tombol back
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set judul ke tengah manual
        val toolbarTitle = binding.toolbarBooking.findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle?.text = "Booking Servis Bengkel"

        // Tombol kembali
        binding.toolbarBooking.setNavigationOnClickListener {
            finish()
        }

        setupServiceSpinner()
        setupTimeSpinner()
        setupDatePicker()
        setupConfirmButton()
        setupBottomNavButtons() // ← Tambahkan ini
    }

    private fun setupServiceSpinner() {
        val services = listOf("Ganti Oli", "Ganti Ban", "Boreup", "Tune Up", "Infus Injeksi", "Dyno Test")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerService.adapter = adapter
    }

    private fun setupTimeSpinner() {
        val times = listOf("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTime.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, y, m, d ->
                val dateStr = "${d.toString().padStart(2, '0')}-${(m + 1).toString().padStart(2, '0')}-$y"
                binding.btnDate.text = dateStr
            }, year, month, day)

            dpd.datePicker.minDate = calendar.timeInMillis
            dpd.show()
        }
    }

    private fun setupConfirmButton() {
        binding.btnConfirm.setOnClickListener {
            val layanan = binding.spinnerService.selectedItem.toString()
            val tanggal = binding.btnDate.text.toString()
            val jam = binding.spinnerTime.selectedItem.toString()
            val brand = binding.editBrand.text.toString()
            val model = binding.editModel.text.toString()
            val plat = binding.editPlat.text.toString()
            val keluhan = binding.editKeluhan.text.toString()

            if (model.isBlank() || plat.isBlank() || keluhan.isBlank() || tanggal == "Pilih Tanggal") {
                Toast.makeText(this, "Mohon isi semua data dengan lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Booking berhasil:\nLayanan: $layanan\nTanggal: $tanggal\nJam: $jam\nBrand: $brand\nModel: $model\nPlat: $plat\nKeluhan: $keluhan",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupBottomNavButtons() {
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.fabHome.setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
        }
    }
}
