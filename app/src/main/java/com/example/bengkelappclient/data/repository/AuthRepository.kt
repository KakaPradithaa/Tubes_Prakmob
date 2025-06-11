package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.local.dao.UserDao
import com.example.bengkelappclient.data.model.AuthResponse
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val userPreferenceManager: UserPreferenceManager
) {

    val isLoggedIn: Flow<Boolean> = userPreferenceManager.isLoggedIn
    val authToken: Flow<String?> = userPreferenceManager.authToken

    suspend fun register(name: String, email: String, pass: String, passConfirm: String, phone: String, address: String): Result<AuthResponse> {
        return try {
            val response = apiService.registerUser(
                mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to pass,
                    "confirm_password" to passConfirm,
                    "phone" to phone,
                    "address" to address
                )
            )
            if (response.isSuccessful && response.body() != null) {
                // Simpan semua data user ke DataStore setelah register
                response.body()?.data?.let { user ->
                    userPreferenceManager.saveAuthToken(user.token)
                    userPreferenceManager.saveUserId(user.id)
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // BARU: Menyimpan role pengguna
                    userPreferenceManager.saveUserRole(user.role)
                }
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()} ${response.message()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, pass: String): Result<AuthResponse> {
        return try {
            val response = apiService.loginUser(
                mapOf(
                    "email" to email,
                    "password" to pass
                )
            )
            if (response.isSuccessful && response.body() != null) {
                // Simpan semua data user ke DataStore setelah login
                response.body()?.data?.let { user ->
                    userPreferenceManager.saveAuthToken(user.token)
                    userPreferenceManager.saveUserId(user.id)
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // BARU: Menyimpan role pengguna
                    userPreferenceManager.saveUserRole(user.role)
                }
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()} - ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            // Selalu bersihkan data lokal, tidak peduli API logout berhasil atau tidak
            userPreferenceManager.clearAuthToken()

            // Mencoba memanggil API logout, tapi tidak menghentikan proses jika gagal
            try {
                apiService.logoutUser()
            } catch (apiError: Exception) {
                println("API logout call failed, but user data cleared locally. Error: ${apiError.message}")
            }


            Result.success(Unit)
        } catch (e: Exception) {
            // Jika ada error saat membersihkan token, tangani di sini
            Result.failure(e)
        }
    }

    fun getCurrentUserName(): Flow<String?> = userPreferenceManager.userName
    fun getCurrentUserEmail(): Flow<String?> = userPreferenceManager.userEmail
    fun getCurrentUserId(): Flow<Int?> = userPreferenceManager.userId
    // BARU: Tambahkan getter untuk role jika dibutuhkan di tempat lain
    fun getCurrentUserRole(): Flow<String?> = userPreferenceManager.userRole
}