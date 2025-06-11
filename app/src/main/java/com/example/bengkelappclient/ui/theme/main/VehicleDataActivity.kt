package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.example.bengkelappclient.ui.theme.order.OrderStatusActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.example.bengkelappclient.util.Resource // Import Resource
import dagger.hilt.android.AndroidEntryPoint // Import AndroidEntryPoint

@AndroidEntryPoint // Tambahkan anotasi ini
class VehicleDataActivity : AppCompatActivity() {

    private lateinit var backIcon: ImageButton
    private lateinit var editBrand: TextInputEditText
    private lateinit var editModel: TextInputEditText
    private lateinit var editTahun: TextInputEditText
    private lateinit var editPlat: TextInputEditText
    private lateinit var btnConfirm: MaterialButton
    private lateinit var navHistory: ImageButton
    private lateinit var navProfile: ImageButton
    private lateinit var fabHome: FloatingActionButton

    // Inisialisasi ViewModel
    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicles)

        // Initialize views
        backIcon = findViewById(R.id.back_icon)
        editBrand = findViewById(R.id.editBrand)
        editModel = findViewById(R.id.editModel)
        editTahun = findViewById(R.id.editTahun)
        editPlat = findViewById(R.id.editPlat)
        btnConfirm = findViewById(R.id.btnConfirm)
        navHistory = findViewById(R.id.nav_history)
        navProfile = findViewById(R.id.nav_profile)
        fabHome = findViewById(R.id.fab_home)

        // Set up click listeners
        backIcon.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnConfirm.setOnClickListener {
            saveVehicleData()
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

        // Amati hasil operasi dari ViewModel
        observeViewModel()
    }

    private fun saveVehicleData() {
        val brand = editBrand.text.toString().trim()
        val model = editModel.text.toString().trim()
        val tahun = editTahun.text.toString().trim()
        val platNomor = editPlat.text.toString().trim()

        if (brand.isEmpty() || model.isEmpty() || tahun.isEmpty() || platNomor.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Panggil fungsi createVehicle dari ViewModel
        vehicleViewModel.createVehicle(brand, model, tahun, platNomor)
    }

    private fun observeViewModel() {
        vehicleViewModel.operationResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Tampilkan loading indicator (misalnya ProgressBar)
                        // Untuk saat ini, kita bisa menonaktifkan tombol
                        btnConfirm.isEnabled = false
                        Toast.makeText(this, "Menyimpan data kendaraan...", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        btnConfirm.isEnabled = true
                        Toast.makeText(this, resource.data, Toast.LENGTH_LONG).show()
                        clearInputFields() // Bersihkan input setelah berhasil
                        // Anda bisa menambahkan navigasi ke daftar kendaraan atau halaman lain di sini
                        finish() // Kembali ke halaman sebelumnya setelah sukses
                    }
                    is Resource.Error -> {
                        btnConfirm.isEnabled = true
                        val errorMessage = resource.message ?: "Terjadi kesalahan."
                        Toast.makeText(this, "Gagal: $errorMessage", Toast.LENGTH_LONG).show()
                        Log.e("VehicleDataActivity", "Error: $errorMessage")
                    }
                }
            }
        }
    }

    private fun clearInputFields() {
        editBrand.text?.clear()
        editModel.text?.clear()
        editTahun.text?.clear()
        editPlat.text?.clear()
    }
}