package com.example.bengkelappclient.data.local.dao

import androidx.room.*
import com.example.bengkelappclient.data.local.entity.ServiceEntity

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(services: List<ServiceEntity>)

    @Query("SELECT * FROM services")
    suspend fun getAllServices(): List<ServiceEntity>
}
