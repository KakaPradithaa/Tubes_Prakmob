package com.example.bengkelappclient.ui.theme.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.model.ServiceResult
import com.example.bengkelappclient.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingHistory = MutableLiveData<ServiceResult<List<BookingDetails>>>()
    val bookingHistory: LiveData<ServiceResult<List<BookingDetails>>> = _bookingHistory

    fun fetchBookingHistory() {
        viewModelScope.launch {
            _bookingHistory.value = ServiceResult.Loading
            val result = bookingRepository.getBookingDetailsForCurrentUser()
            result.fold(
                onSuccess = { bookingDetailsList ->
                    _bookingHistory.value = ServiceResult.Success(bookingDetailsList)
                },
                onFailure = { exception ->
                    _bookingHistory.value = ServiceResult.Error(exception, exception.message ?: "Failed to fetch booking history")
                }
            )
        }
    }
}