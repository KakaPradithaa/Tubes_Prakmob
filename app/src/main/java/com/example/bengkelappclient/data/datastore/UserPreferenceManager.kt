package com.example.bengkelappclient.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bengkel_user_preferences")

@Singleton
class UserPreferenceManager @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        // BARU: Tambahkan kunci untuk menyimpan role pengguna
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    // --- Auth Token ---
    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferenceKeys.AUTH_TOKEN]
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.AUTH_TOKEN] = token
        }
    }

    // DIUBAH: Tambahkan penghapusan role saat logout
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.AUTH_TOKEN)
            preferences.remove(PreferenceKeys.USER_ID)
            preferences.remove(PreferenceKeys.USER_NAME)
            preferences.remove(PreferenceKeys.USER_EMAIL)
            // BARU: Hapus role pengguna saat logout
            preferences.remove(PreferenceKeys.USER_ROLE)
        }
    }

    // --- User Data ---
    val userId: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.USER_ID]
    }

    suspend fun saveUserId(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_ID] = id
        }
    }

    val userName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.USER_NAME]
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_NAME] = name
        }
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.USER_EMAIL]
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_EMAIL] = email
        }
    }

    // =============================================================
    // BARU: Fungsi untuk menyimpan dan mendapatkan ROLE pengguna
    // =============================================================
    /**
     * Flow untuk membaca role pengguna secara real-time.
     * BookingRepository akan menggunakan ini.
     */
    val userRole: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.USER_ROLE]
    }

    /**
     * Fungsi untuk menyimpan role pengguna setelah berhasil login.
     */
    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.USER_ROLE] = role
        }
    }
    // =============================================================

    val isLoggedIn: Flow<Boolean> = authToken.map { it != null }
}