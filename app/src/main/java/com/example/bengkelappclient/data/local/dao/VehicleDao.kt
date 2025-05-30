package com.example.bengkelappclient.data.local.dao

import androidx.room.*
import com.example.bengkelappclient.data.local.entity.VehicleEntity

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles WHERE user_id = :userId")
    suspend fun getVehiclesByUser(userId: Int): List<VehicleEntity>

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)
}
