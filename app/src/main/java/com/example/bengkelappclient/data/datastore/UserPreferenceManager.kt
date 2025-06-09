package com.example.bengkelappclient.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey // Pastikan import ini ada
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bengkel_user_preferences") // Memberi nama yang lebih deskriptif

@Singleton
class UserPreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Menggunakan 'private object' untuk mengelompokkan keys
    private object PreferenceKeys {
        val AUTH_TOKEN = stringPreferencesKey(name = "auth_token")
        val USER_NAME = stringPreferencesKey(name = "user_name")
        val USER_EMAIL = stringPreferencesKey(name = "user_email")
        val USER_ID = intPreferencesKey(name = "user_id") // BARU: Tambahkan kunci untuk ID pengguna
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
            preferences.remove(PreferenceKeys.USER_ID) // BARU: Hapus ID pengguna saat logout
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

    // BARU: Fungsi untuk menyimpan dan mendapatkan ID pengguna
    val userId: Flow<Int?> = context.dataStore.data
        .map { prefs ->
            prefs[PreferenceKeys.USER_ID]
        }

    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_ID] = id
        }
    }

    // Fungsi untuk mengecek apakah user sudah login (berdasarkan ada tidaknya token)
    val isLoggedIn: Flow<Boolean> = authToken.map { it != null }
}
