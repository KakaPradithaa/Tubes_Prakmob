package com.example.bengkelappclient.ui.theme.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.model.StatusUpdatePayload
import com.example.bengkelappclient.data.repository.BookingRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrderViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _updateStatusResult = MutableLiveData<Event<Resource<BookingDetails>>>()
    val updateStatusResult: LiveData<Event<Resource<BookingDetails>>> = _updateStatusResult

    fun updateBookingStatus(bookingId: Int, newStatus: String) {
        viewModelScope.launch {
            _updateStatusResult.postValue(Event(Resource.Loading()))
            try {
                val payload = StatusUpdatePayload(newStatus)
                val result = bookingRepository.updateBookingStatus(bookingId, payload)

                result.fold(
                    onSuccess = { updateBookingResponse ->
                        // updateBookingResponse.booking adalah objek Booking yang diperbarui
                        // Anda bisa mengonversinya ke BookingDetails jika perlu atau langsung pakai Booking
                        _updateStatusResult.postValue(Event(Resource.Success(
                            BookingDetails(
                                booking = updateBookingResponse.booking,
                                vehicle = updateBookingResponse.booking.vehicle!!, // Pastikan tidak null
                                service = updateBookingResponse.booking.service!! // Pastikan tidak null
                            )
                        )))
                    },
                    onFailure = { exception ->
                        _updateStatusResult.postValue(Event(Resource.Error(exception.message ?: "Gagal memperbarui status")))
                    }
                )
            } catch (e: Exception) {
                _updateStatusResult.postValue(Event(Resource.Error(e.message ?: "Terjadi kesalahan jaringan")))
            }
        }
    }
}