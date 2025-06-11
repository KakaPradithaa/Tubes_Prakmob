package com.example.bengkelappclient // Pastikan package-nya sesuai

import android.app.Application
import com.example.bengkelappclient.data.remote.ApiClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Anotasi Hilt ini tetap dipertahankan
class BengkelApplication : Application() {

    // TAMBAHKAN BLOK INI
    override fun onCreate() {
        super.onCreate()

        // Panggil inisialisasi untuk ApiClient manual Anda di sini.
        // Ini akan berjalan satu kali saat aplikasi pertama kali dibuat.
        ApiClient.initialize(this)
    }
}
