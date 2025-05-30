// data/local/entity/UserEntity.kt
package com.example.bengkelappclient.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String
    // Tambahkan field lain yang ingin disimpan offline jika perlu
)