// data/local/dao/UserDao.kt
package com.example.bengkelappclient.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bengkelappclient.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clearUser() // Hapus semua user (jika hanya satu user yang login)
}