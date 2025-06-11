package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.example.bengkelappclient.data.model.Service
import com.example.bengkelappclient.data.model.Vehicle
import com.example.bengkelappclient.data.model.Schedule // Import Schedule model
import com.example.bengkelappclient.ui.service.ServiceViewModel
import com.example.bengkelappclient.ui.theme.booking.BookingViewModel
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import com.example.bengkelappclient.ui.schedule.ScheduleViewModel // Import ScheduleViewModel
import com.example.bengkelappclient.util.Resource
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookingServiceActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageButton
    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerService: Spinner
    private lateinit var spinnerDate: Spinner
    private lateinit var editJam: EditText
    private lateinit var editKeluhan: TextInputEditText
    private lateinit var btnConfirm: MaterialButton
    private lateinit var navHistory: ImageButton
    private lateinit var navProfile: ImageButton
    private lateinit var fabHome: FloatingActionButton

    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val serviceViewModel: ServiceViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels() // Dapatkan instance ScheduleViewModel

    private var vehiclesList: List<Vehicle> = emptyList()
    private var servicesList: List<Service> = emptyList()
    private var schedulesList: List<Schedule> = emptyList() // Daftar jadwal dari API

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

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

        setupSpinnersInitially()
        observeViewModels()

        // Mulai pengambilan data dari semua ViewModel
        vehicleViewModel.fetchMyVehicles()
        serviceViewModel.fetchAllServices()
        scheduleViewModel.getSchedules() // Memanggil fungsi untuk mendapatkan jadwal

        backIcon.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnConfirm.setOnClickListener {
            handleBookingConfirmation()
        }

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

    private fun setupSpinnersInitially() {
        val initialBrands = listOf("Memuat Kendaraan...")
        val brandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialBrands)
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBrand.adapter = brandAdapter
        spinnerBrand.isEnabled = false

        val initialServices = listOf("Memuat Layanan...")
        val serviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialServices)
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerService.adapter = serviceAdapter
        spinnerService.isEnabled = false

        // Spinner untuk hari akan diisi dari data jadwal
        val initialDays = listOf("Memuat Hari...")
        val dateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialDays)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDate.adapter = dateAdapter
        spinnerDate.isEnabled = false
    }

    private fun observeViewModels() {
        // ... (observe vehicleViewModel.vehicles - kode yang sudah ada)
        vehicleViewModel.vehicles.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    spinnerBrand.isEnabled = false
                    val loadingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Memuat Kendaraan..."))
                    loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerBrand.adapter = loadingAdapter
                }
                is Resource.Success -> {
                    vehiclesList = resource.data ?: emptyList()
                    if (vehiclesList.isEmpty()) {
                        val emptyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Tidak ada kendaraan"))
                        emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerBrand.adapter = emptyAdapter
                        spinnerBrand.isEnabled = false
                        Toast.makeText(this, "Tidak ada kendaraan yang terdaftar.", Toast.LENGTH_SHORT).show()
                    } else {
                        val vehicleNames = vehiclesList.map { "${it.brand} - ${it.licensePlate}" }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerBrand.adapter = adapter
                        spinnerBrand.isEnabled = true
                    }
                }
                is Resource.Error -> {
                    spinnerBrand.isEnabled = false
                    val errorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Gagal memuat kendaraan"))
                    errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerBrand.adapter = errorAdapter
                    Toast.makeText(this, "Gagal memuat kendaraan: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // ... (observe serviceViewModel.serviceList - kode yang sudah ada)
        serviceViewModel.serviceList.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    spinnerService.isEnabled = false
                    val loadingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Memuat Layanan..."))
                    loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerService.adapter = loadingAdapter
                }
                is Resource.Success -> {
                    servicesList = resource.data ?: emptyList()
                    if (servicesList.isEmpty()) {
                        val emptyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Tidak ada layanan"))
                        emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerService.adapter = emptyAdapter
                        spinnerService.isEnabled = false
                        Toast.makeText(this, "Tidak ada layanan yang tersedia.", Toast.LENGTH_SHORT).show()
                    } else {
                        val serviceNames = servicesList.map { it.name }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerService.adapter = adapter
                        spinnerService.isEnabled = true
                    }
                }
                is Resource.Error -> {
                    spinnerService.isEnabled = false
                    val errorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Gagal memuat layanan"))
                    errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerService.adapter = errorAdapter
                    Toast.makeText(this, "Gagal memuat layanan: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // BARU: Mengamati daftar jadwal dari ScheduleViewModel
        scheduleViewModel.schedules.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    spinnerDate.isEnabled = false
                    val loadingAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Memuat Hari..."))
                    loadingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerDate.adapter = loadingAdapter
                }
                is Resource.Success -> {
                    schedulesList = resource.data ?: emptyList()
                    if (schedulesList.isEmpty()) {
                        val emptyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Tidak ada jadwal tersedia"))
                        emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerDate.adapter = emptyAdapter
                        spinnerDate.isEnabled = false
                        Toast.makeText(this, "Tidak ada jadwal servis yang tersedia.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Ambil daftar hari unik dari jadwal yang tersedia
                        val availableDays = schedulesList.map { it.day }.distinct()
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableDays)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerDate.adapter = adapter
                        spinnerDate.isEnabled = true
                    }
                }
                is Resource.Error -> {
                    spinnerDate.isEnabled = false
                    val errorAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Gagal memuat jadwal"))
                    errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerDate.adapter = errorAdapter
                    Toast.makeText(this, "Gagal memuat jadwal: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // ... (observe bookingViewModel.bookingResult - kode yang sudah ada)
        bookingViewModel.bookingResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        btnConfirm.isEnabled = false
                        Toast.makeText(this, "Membuat booking...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        btnConfirm.isEnabled = true
                        Toast.makeText(this, resource.data, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    is Resource.Error -> {
                        btnConfirm.isEnabled = true
                        Toast.makeText(this, resource.message ?: "Gagal membuat booking", Toast.LENGTH_LONG).show()
                        Log.e("BookingServiceActivity", "Error booking: ${resource.message}")
                    }
                }
            }
        }
    }

    private fun handleBookingConfirmation() {
        val selectedVehicleIndex = spinnerBrand.selectedItemPosition
        val selectedServiceIndex = spinnerService.selectedItemPosition
        val selectedDayString = spinnerDate.selectedItem?.toString() ?: "" // Ambil hari yang dipilih
        val jam = editJam.text.toString().trim()
        val keluhan = editKeluhan.text.toString().trim()

        if (selectedVehicleIndex < 0 || selectedVehicleIndex >= vehiclesList.size ||
            selectedServiceIndex < 0 || selectedServiceIndex >= servicesList.size ||
            selectedDayString.isEmpty() || selectedDayString == "Tidak ada jadwal tersedia" || jam.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data wajib dan pastikan data kendaraan/layanan/jadwal sudah termuat.", Toast.LENGTH_SHORT).show()
            return
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val parsedTime: Date? = try {
            timeFormat.parse(jam)
        } catch (e: Exception) {
            null
        }

        if (parsedTime == null) {
            Toast.makeText(this, "Format jam tidak valid. Gunakan format HH:mm (misal: 08:00)", Toast.LENGTH_SHORT).show()
            return
        }

        val formattedTime = timeFormat.format(parsedTime)

        val selectedVehicle = vehiclesList[selectedVehicleIndex]
        val selectedService = servicesList[selectedServiceIndex]

        // Mencari scheduleId yang sesuai dengan hari yang dipilih
        val selectedSchedule = schedulesList.firstOrNull { it.day == selectedDayString }

        if (selectedSchedule == null) {
            Toast.makeText(this, "Jadwal untuk hari yang dipilih tidak ditemukan.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        bookingViewModel.createBooking(
            vehicleId = selectedVehicle.id,
            serviceId = selectedService.id,
            // scheduleId: kita perlu mengirim ID dari jadwal yang dipilih
            // Ini adalah contoh bagaimana scheduleId bisa di passing.
            // Pastikan API Anda menerima schedule_id.
            // Saat ini Booking model memiliki scheduleId sebagai Int?, jadi bisa null.
            // Jika backend mengharuskan, pastikan ini tidak null.
            scheduleId = selectedSchedule.id, // Mengirim scheduleId dari jadwal yang dipilih
            bookingDate = currentDate,
            bookingTime = formattedTime,
            complaint = if (keluhan.isNotEmpty()) keluhan else null
        )
    }
}