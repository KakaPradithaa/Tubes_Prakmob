// data/local/dao/CustomerDao.kt
package com.example.bengkelappclient.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bengkelappclient.data.local.entity.CustomerEntity

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCustomers(customers: List<CustomerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): LiveData<List<CustomerEntity>> // LiveData untuk observasi UI

    @Query("SELECT * FROM customers WHERE id = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Int): CustomerEntity?

    @Query("DELETE FROM customers WHERE id = :customerId")
    suspend fun deleteCustomerById(customerId: Int)

    @Query("DELETE FROM customers")
    suspend fun clearAllCustomers()
}