package com.example.bengkelappclient.ui.theme.main

import android.content.Intent // Penting: Impor untuk Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class BookingServiceActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageButton
    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerService: Spinner
    private lateinit var spinnerDate: Spinner // Spinner ini sekarang akan menampung hari dalam seminggu
    private lateinit var editJam: EditText
    private lateinit var editKeluhan: TextInputEditText
    private lateinit var btnConfirm: MaterialButton
    private lateinit var navHistory: ImageButton
    private lateinit var navProfile: ImageButton
    private lateinit var fabHome: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pastikan nama file XML Anda adalah activity_booking.xml atau activity_booking_service.xml
        setContentView(R.layout.activity_booking)

        // Inisialisasi tampilan (views)
        backIcon = findViewById(R.id.back_icon)
        spinnerBrand = findViewById(R.id.spinnerBrand)
        spinnerService = findViewById(R.id.spinnerService)
        spinnerDate = findViewById(R.id.spinnerDate)
        editJam = findViewById(R.id.editJam)
        editKeluhan = findViewById(R.id.editKeluhan)
        btnConfirm = findViewById(R.id.btnConfirm)
        navHistory = findViewById(R.id.nav_history)
        navProfile = findViewById(R.id.nav_profile)
        fabHome = findViewById(R.id.fab_home)

        // Siapkan Spinner dengan data yang sesuai
        setupSpinners()

        // Atur pendengar klik
        backIcon.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Menangani penekanan tombol kembali
        }

        btnConfirm.setOnClickListener {
            handleBookingConfirmation()
        }

        // --- Logika Navigasi Bilah Bawah (Ditambahkan di sini) ---
        navHistory.setOnClickListener {
            startActivity(Intent(this, OrderStatusActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        fabHome.setOnClickListener {
            startActivity(Intent(this, homepage::class.java))
        }
    }

    private fun setupSpinners() {
        // Data dummy untuk Spinner Merek (Anda akan menggantinya dengan data sesungguhnya)
        val brands = arrayOf("Pilih Merek Kendaraan", "Toyota", "Honda", "Suzuki", "Mitsubishi")
        val brandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, brands)
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBrand.adapter = brandAdapter

        // Data dummy untuk Spinner Layanan Servis (Anda akan menggantinya dengan data sesungguhnya)
        val services = arrayOf("Pilih Layanan Servis", "Servis Berkala", "Ganti Oli", "Perbaikan Rem", "Lain-lain")
        val serviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, services)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerService.adapter = serviceAdapter

        // Diperbarui: Data untuk Spinner Tanggal/Hari untuk menyertakan hari-hari dalam seminggu
        val daysOfWeek = arrayOf("Pilih Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
        val dateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daysOfWeek)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDate.adapter = dateAdapter
    }

    private fun handleBookingConfirmation() {
        val selectedBrand = spinnerBrand.selectedItem.toString()
        val selectedService = spinnerService.selectedItem.toString()
        val selectedDay = spinnerDate.selectedItem.toString() // Mengubah nama variabel untuk mencerminkan 'hari'
        val jam = editJam.text.toString().trim()
        val keluhan = editKeluhan.text.toString().trim()

        // Validasi dasar
        if (selectedBrand == "Pilih Merek Kendaraan" ||
            selectedService == "Pilih Layanan Servis" ||
            selectedDay == "Pilih Hari" || // Validasi diperbarui untuk 'hari'
            jam.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data wajib.", Toast.LENGTH_SHORT).show()
            return
        }

        // Di sini Anda biasanya akan mengirim data ini ke server atau memprosesnya lebih lanjut
        val confirmationMessage = """
            Konfirmasi Booking:
            Kendaraan: $selectedBrand
            Layanan: $selectedService
            Hari: $selectedDay
            Jam: $jam
            Keluhan: ${if (keluhan.isNotEmpty()) keluhan else "-"}
        """.trimIndent()

        Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show()

        // Anda mungkin ingin menavigasi ke layar sukses atau menampilkan dialog di sini
    }
}