// data/repository/AuthRepository.kt
package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.local.dao.UserDao
import com.example.bengkelappclient.data.local.entity.UserEntity
import com.example.bengkelappclient.data.model.AuthResponse
import com.example.bengkelappclient.data.model.User
import com.example.bengkelappclient.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao, // Jika ingin menyimpan user info di Room
    private val userPreferenceManager: UserPreferenceManager
) {

    val isLoggedIn: Flow<Boolean> = userPreferenceManager.isLoggedIn
    val authToken: Flow<String?> = userPreferenceManager.authToken

    suspend fun register(name: String, email: String, pass: String, passConfirm: String): Result<AuthResponse> {
        return try {
            val response = apiService.registerUser(
                mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to pass,
                    "password_confirmation" to passConfirm
                )
            )
            if (response.isSuccessful && response.body() != null) {
                response.body()?.accessToken?.let { token ->
                    userPreferenceManager.saveAuthToken(token)
                }
                response.body()?.user?.let { user ->
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // Opsional: simpan user ke Room
                    // userDao.insertUser(UserEntity(user.id, user.name, user.email))
                }
                Result.success(response.body()!!)
            } else {
                // Tangani error API (misal, parse error body jika ada)
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
                response.body()?.accessToken?.let { token ->
                    userPreferenceManager.saveAuthToken(token)
                }
                response.body()?.user?.let { user ->
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // Opsional: simpan user ke Room
                    // userDao.insertUser(UserEntity(user.id, user.name, user.email))
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
            // Pastikan token ada sebelum mencoba logout dari API
            if (userPreferenceManager.getToken().first() != null) {
                val response = apiService.logoutUser() // API call
                if (!response.isSuccessful) {
                    // Log error, tapi tetap lanjutkan proses logout lokal
                    println("API logout failed: ${response.code()} ${response.message()}")
                }
            }
            // Selalu bersihkan token lokal dan data user
            userPreferenceManager.clearAuthToken()
            // Opsional: bersihkan user dari Room
            // userDao.clearUser()
            Result.success(Unit)
        } catch (e: Exception) {
            // Jika API call gagal karena network error, tetap bersihkan token lokal
            userPreferenceManager.clearAuthToken()
            // userDao.clearUser()
            Result.failure(e) // Atau Result.success(Unit) jika logout lokal dianggap cukup
        }
    }

    suspend fun getUserProfileFromApi(): Result<User?> {
        return try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful && response.body()?.user != null) {
                Result.success(response.body()!!.user)
            } else {
                Result.failure(Exception("Failed to fetch user profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Mengambil data user dari DataStore
    fun getCurrentUserName(): Flow<String?> = userPreferenceManager.userName
    fun getCurrentUserEmail(): Flow<String?> = userPreferenceManager.userEmail

}