// data/datastore/UserPreferenceManager.kt
package com.example.bengkelappclient.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey // Pastikan import ini benar
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Deklarasikan DataStore di top-level file (cukup sekali per aplikasi)
// Nama "user_prefs" adalah nama file preferences yang akan dibuat.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bengkel_user_preferences") // Memberi nama yang lebih deskriptif

@Singleton
class UserPreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Menggunakan 'private object' untuk mengelompokkan keys
    private object PreferenceKeys { // <--- PERBAIKAN DI SINI (nama object)
        // Menggunakan named argument 'name' untuk stringPreferencesKey
        val AUTH_TOKEN = stringPreferencesKey(name = "auth_token") // <--- PERBAIKAN DI SINI
        val USER_NAME = stringPreferencesKey(name = "user_name")   // <--- PERBAIKAN DI SINI
        val USER_EMAIL = stringPreferencesKey(name = "user_email") // <--- PERBAIKAN DI SINI
        // Tambahkan key lain jika perlu
    }

    // --- Auth Token ---
    val authToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.AUTH_TOKEN]
        }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.AUTH_TOKEN] = token
        }
    }

    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.AUTH_TOKEN)
            // Juga hapus info user lain saat logout
            preferences.remove(PreferenceKeys.USER_NAME)
            preferences.remove(PreferenceKeys.USER_EMAIL)
        }
    }

    // Fungsi untuk mendapatkan token (digunakan di AuthInterceptor jika perlu akses non-suspend,
    // tapi umumnya interceptor juga bisa dibuat suspend atau menggunakan runBlocking dengan hati-hati)
    // Untuk AuthInterceptor yang sudah kita buat dengan runBlocking, ini sudah OK.
    fun getToken(): Flow<String?> = authToken


    // --- Contoh menyimpan data user lain ---
    val userName: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[PreferenceKeys.USER_NAME]
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_NAME] = name
        }
    }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[PreferenceKeys.USER_EMAIL]
        }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_EMAIL] = email
        }
    }

    // Fungsi untuk mengecek apakah user sudah login (berdasarkan ada tidaknya token)
    val isLoggedIn: Flow<Boolean> = authToken.map { it != null }
}