package com.example.bengkelappclient.ui.theme.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.repository.BookingRepository
import com.example.bengkelappclient.util.Resource // Pastikan import Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingCounts = MutableLiveData<Resource<Map<String, Int>>>()
    val bookingCounts: LiveData<Resource<Map<String, Int>>> = _bookingCounts

    init {
        fetchBookingCounts()
    }

    fun fetchBookingCounts() {
        viewModelScope.launch {
            _bookingCounts.postValue(Resource.Loading())
            val result = bookingRepository.getBookingDetails() // Mengambil semua booking details

            result.fold(
                onSuccess = { bookingDetailsList ->
                    val counts = bookingDetailsList
                        .groupBy { it.booking.status.lowercase() }
                        .mapValues { it.value.size }
                    _bookingCounts.postValue(Resource.Success(counts))
                },
                onFailure = { exception ->
                    _bookingCounts.postValue(Resource.Error(exception.message ?: "Gagal memuat statistik pesanan"))
                }
            )
        }
    }
}