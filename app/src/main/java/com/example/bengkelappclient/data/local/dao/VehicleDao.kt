package com.example.bengkelappclient.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bengkelappclient.data.local.entity.VehicleEntity

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVehicles(vehicles: List<VehicleEntity>)

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles_table WHERE id = :vehicleId LIMIT 1")
    suspend fun getVehicleById(vehicleId: Int): VehicleEntity?

    @Query("SELECT * FROM vehicles_table WHERE customerId = :customerId ORDER BY brand ASC, model ASC")
    fun getVehiclesByCustomerId(customerId: Int): LiveData<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles_table ORDER BY brand ASC, model ASC")
    fun getAllVehicles(): LiveData<List<VehicleEntity>> // Tambahan jika ingin semua kendaraan tanpa filter customer

    @Query("DELETE FROM vehicles_table WHERE id = :vehicleId")
    suspend fun deleteVehicleById(vehicleId: Int)

    @Query("DELETE FROM vehicles_table WHERE customerId = :customerId")
    suspend fun deleteVehiclesByCustomerId(customerId: Int) // Jika customer dihapus, dan onDelete CASCADE tidak cukup/perlu manual

    @Query("DELETE FROM vehicles_table")
    suspend fun clearAllVehicles()
}