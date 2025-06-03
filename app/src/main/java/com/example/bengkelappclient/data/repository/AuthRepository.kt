package com.example.bengkelappclient.data.repository

import com.example.bengkelappclient.data.datastore.UserPreferenceManager
import com.example.bengkelappclient.data.local.dao.UserDao
import com.example.bengkelappclient.data.model.AuthResponse
import com.example.bengkelappclient.data.model.UserData
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
                response.body()?.data?.let { user ->
                    userPreferenceManager.saveAuthToken(user.token)
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // Optional simpan ke Room
                    // userDao.insertUser(UserEntity(0, user.name, user.email))
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
                response.body()?.data?.let { user ->
                    userPreferenceManager.saveAuthToken(user.token)
                    userPreferenceManager.saveUserName(user.name)
                    userPreferenceManager.saveUserEmail(user.email)
                    // Optional simpan ke Room
                    // userDao.insertUser(UserEntity(0, user.name, user.email))
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
            if (userPreferenceManager.getToken().first() != null) {
                val response = apiService.logoutUser()
                if (!response.isSuccessful) {
                    println("API logout failed: ${response.code()} ${response.message()}")
                }
            }
            userPreferenceManager.clearAuthToken()
            Result.success(Unit)
        } catch (e: Exception) {
            userPreferenceManager.clearAuthToken()
            Result.failure(e)
        }
    }

    fun getCurrentUserName(): Flow<String?> = userPreferenceManager.userName
    fun getCurrentUserEmail(): Flow<String?> = userPreferenceManager.userEmail
}