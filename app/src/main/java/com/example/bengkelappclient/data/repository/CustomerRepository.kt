// data/repository/CustomerRepository.kt
package com.example.bengkelappclient.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.bengkelappclient.data.local.dao.CustomerDao
import com.example.bengkelappclient.data.local.entity.CustomerEntity
import com.example.bengkelappclient.data.model.Customer
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Helper untuk konversi (bisa ditaruh di file mapper terpisah)
fun Customer.toEntity(): CustomerEntity = CustomerEntity(this.id, this.name, this.phoneNumber, this.address)
fun CustomerEntity.toDomain(): Customer = Customer(this.id, this.name, this.phoneNumber, this.address, null, null)
fun List<CustomerEntity>.toDomainList(): List<Customer> = this.map { it.toDomain() }
fun List<Customer>.toEntityList(): List<CustomerEntity> = this.map { it.toEntity() }


@Singleton
class CustomerRepository @Inject constructor(
    private val apiService: ApiService,
    private val customerDao: CustomerDao
) {
    // Mengambil semua customer dari Room, otomatis update jika data di Room berubah
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers().map { entities ->
        entities.toDomainList()
    }

    // Fungsi untuk me-refresh data customer dari API dan menyimpannya ke Room
    suspend fun refreshCustomersFromApi(): Result<Unit> {
        return withContext(Dispatchers.IO) { // Operasi network dan DB di IO dispatcher
            try {
                val response = apiService.getAllCustomers()
                if (response.isSuccessful && response.body() != null) {
                    val customersFromApi = response.body()!!
                    customerDao.clearAllCustomers() // Hapus data lama
                    customerDao.insertAllCustomers(customersFromApi.toEntityList()) // Simpan data baru
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to fetch customers: ${response.code()} ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun addCustomer(customerName: String, phone: String, address: String?): Result<Customer> {
        return withContext(Dispatchers.IO) {
            try {
                // Buat DTO Customer untuk dikirim ke API (id bisa 0 atau diabaikan oleh API saat create)
                val newCustomerDto = Customer(
                    id = 0, // ID akan digenerate oleh server
                    name = customerName,
                    phoneNumber = phone,
                    address = address,
                    createdAt = null,
                    updatedAt = null
                )
                val response = apiService.createCustomer(newCustomerDto)
                if (response.isSuccessful && response.body() != null) {
                    val createdCustomer = response.body()!!
                    customerDao.insertCustomer(createdCustomer.toEntity()) // Simpan ke Room juga
                    Result.success(createdCustomer)
                } else {
                    Result.failure(Exception("Failed to add customer: ${response.code()} ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // TODO: Implementasikan updateCustomer dan deleteCustomer serupa
    // deleteCustomer harus menghapus dari API dan kemudian dari Room.
    // updateCustomer harus update di API, dapatkan respons, lalu update di Room.
}