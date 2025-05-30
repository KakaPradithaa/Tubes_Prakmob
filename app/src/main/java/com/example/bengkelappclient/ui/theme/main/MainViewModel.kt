// ui/main/MainViewModel.kt
package com.example.bengkelappclient.ui.theme.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow // Import StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isUserLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn.stateIn( // Tipe diubah ke Boolean?
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = null // <--- DIUBAH MENJADI NULL
    )

    val userName: StateFlow<String?> = authRepository.getCurrentUserName().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L), // <<--- PERBAIKAN DI SINI
        initialValue = null
        )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            // Navigasi akan ditangani oleh observer isUserLoggedIn di SplashActivity atau MainActivity
        }
    }
}