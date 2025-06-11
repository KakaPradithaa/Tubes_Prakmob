package com.example.bengkelappclient // Pastikan package-nya sesuai dengan aplikasi Anda

import android.app.Application
import com.example.bengkelappclient.data.remote.ApiClient

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Panggil method initialize dari ApiClient di sini
        ApiClient.initialize(this)
    }
}