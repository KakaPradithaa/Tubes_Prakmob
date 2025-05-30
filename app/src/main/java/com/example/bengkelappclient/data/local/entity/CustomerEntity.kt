// data/local/entity/CustomerEntity.kt
package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val phoneNumber: String,
    val address: String?
)