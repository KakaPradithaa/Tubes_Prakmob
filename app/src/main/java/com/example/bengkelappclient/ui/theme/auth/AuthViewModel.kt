// ui/auth/AuthViewModel.kt
package com.example.bengkelappclient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.AuthResponse
import com.example.bengkelappclient.data.repository.AuthRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Event<Resource<AuthResponse>>>()
    val loginResult: LiveData<Event<Resource<AuthResponse>>> = _loginResult

    private val _registerResult = MutableLiveData<Event<Resource<AuthResponse>>>()
    val registerResult: LiveData<Event<Resource<AuthResponse>>> = _registerResult

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _loginResult.value = Event(Resource.Loading())
            val result = authRepository.login(email, pass)
            result.fold(
                onSuccess = { authResponse ->
                    _loginResult.value = Event(Resource.Success(authResponse))
                },
                onFailure = { exception ->
                    _loginResult.value = Event(Resource.Error(exception.message ?: "Login failed"))
                }
            )
        }
    }

    fun register(name: String, email: String, pass: String, passConfirm: String, phone: String, address: String) { // Add phone and address
        viewModelScope.launch {
            _registerResult.value = Event(Resource.Loading())
            // Pass phone and address to the repository
            val result = authRepository.register(name, email, pass, passConfirm, phone, address)
            result.fold(
                onSuccess = { authResponse ->
                    _registerResult.value = Event(Resource.Success(authResponse))
                },
                onFailure = { exception ->
                    _registerResult.value = Event(Resource.Error(exception.message ?: "Registration failed"))
                }
            )
        }
    }
}