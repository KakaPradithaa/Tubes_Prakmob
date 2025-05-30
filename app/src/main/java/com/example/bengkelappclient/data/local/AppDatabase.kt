package com.example.bengkelappclient.data.local // PASTIKAN PACKAGE INI BENAR

import android.content.Context
import androidx.room.Database // PASTIKAN IMPORT INI BENAR
import androidx.room.Room
import androidx.room.RoomDatabase
// PASTIKAN SEMUA IMPORT DAO DAN ENTITY DARI PACKAGE YANG BENAR
import com.example.bengkelappclient.data.local.dao.CustomerDao
import com.example.bengkelappclient.data.local.dao.ServiceOrderDao
import com.example.bengkelappclient.data.local.dao.UserDao
import com.example.bengkelappclient.data.local.dao.VehicleDao
import com.example.bengkelappclient.data.local.entity.CustomerEntity
import com.example.bengkelappclient.data.local.entity.ServiceOrderEntity
import com.example.bengkelappclient.data.local.entity.UserEntity
import com.example.bengkelappclient.data.local.entity.VehicleEntity

@Database(
    entities = [
        UserEntity::class,
        CustomerEntity::class,
        VehicleEntity::class,
        ServiceOrderEntity::class
    ],

    version = 1, // Jika ini build pertama, version 1 OK. Jika sudah ada DB, naikkan & migrasi.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao                 // Pastikan UserDao.kt ada dan benar
    abstract fun customerDao(): CustomerDao         // Pastikan CustomerDao.kt ada dan benar
    abstract fun vehicleDao(): VehicleDao           // Pastikan VehicleDao.kt ada dan benar
    abstract fun serviceOrderDao(): ServiceOrderDao // Pastikan ServiceOrderDao.kt ada dan benar

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Di AppDatabase.kt, companion object getInstance()
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bengkel_app_client_db_v1" // Nama database bisa tetap atau diubah
                )
                    .fallbackToDestructiveMigration() // <--- PASTIKAN INI ADA (untuk development)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}