package com.example.bengkelappclient.ui.theme.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bengkelappclient.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicles) // Ensure your XML file is named activity_vehicle_data.xml

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
            onBackPressedDispatcher.onBackPressed() // Handles the back button press
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
    }

    private fun saveVehicleData() {
        val brand = editBrand.text.toString().trim()
        val model = editModel.text.toString().trim()
        val tahun = editTahun.text.toString().trim()
        val platNomor = editPlat.text.toString().trim()

        // Basic validation
        if (brand.isEmpty() || model.isEmpty() || tahun.isEmpty() || platNomor.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // You would typically save this data to a database (local or remote)
        // For demonstration, we'll just show a Toast message.
        val message = """
            Data Kendaraan Disimpan:
            Brand: $brand
            Model: $model
            Tahun: $tahun
            Plat Nomor: $platNomor
        """.trimIndent()

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        // After saving, you might want to navigate back or clear the fields
        // finish() // To go back to the previous activity
        // clearInputFields() // To clear the form
    }

    private fun clearInputFields() {
        editBrand.text?.clear()
        editModel.text?.clear()
        editTahun.text?.clear()
        editPlat.text?.clear()
    }
}