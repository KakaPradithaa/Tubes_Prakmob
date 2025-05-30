package com.example.bengkelappclient.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bengkelappclient.data.local.entity.ServiceOrderEntity

@Dao
interface ServiceOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceOrder(serviceOrder: ServiceOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllServiceOrders(serviceOrders: List<ServiceOrderEntity>)

    @Update
    suspend fun updateServiceOrder(serviceOrder: ServiceOrderEntity)

    @Query("SELECT * FROM service_orders_table WHERE id = :serviceOrderId LIMIT 1")
    suspend fun getServiceOrderById(serviceOrderId: Int): ServiceOrderEntity?

    @Query("SELECT * FROM service_orders_table WHERE vehicleId = :vehicleId ORDER BY entryDate DESC")
    fun getServiceOrdersByVehicleId(vehicleId: Int): LiveData<List<ServiceOrderEntity>>

    @Query("SELECT * FROM service_orders_table ORDER BY entryDate DESC")
    fun getAllServiceOrders(): LiveData<List<ServiceOrderEntity>> // Tambahan jika ingin semua service order

    @Query("DELETE FROM service_orders_table WHERE id = :serviceOrderId")
    suspend fun deleteServiceOrderById(serviceOrderId: Int)

    @Query("DELETE FROM service_orders_table WHERE vehicleId = :vehicleId")
    suspend fun deleteServiceOrdersByVehicleId(vehicleId: Int) // Jika kendaraan dihapus

    @Query("DELETE FROM service_orders_table")
    suspend fun clearAllServiceOrders()
}