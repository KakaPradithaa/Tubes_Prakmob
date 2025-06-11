package com.example.bengkelappclient.ui.theme.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Booking
import com.example.bengkelappclient.data.repository.BookingRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingResult = MutableLiveData<Event<Resource<String>>>()
    val bookingResult: LiveData<Event<Resource<String>>> = _bookingResult

    fun createBooking(
        vehicleId: Int,
        serviceId: Int,
        scheduleId: Int?, // Tambahkan parameter scheduleId
        bookingDate: String,
        bookingTime: String,
        complaint: String?
    ) {
        viewModelScope.launch {
            _bookingResult.postValue(Event(Resource.Loading()))
            try {
                val newBooking = Booking(
                    id = 0,
                    userId = 0, // userId akan diisi oleh repository dari DataStore
                    vehicleId = vehicleId,
                    serviceId = serviceId,
                    scheduleId = scheduleId, // Gunakan parameter scheduleId
                    booking_date = bookingDate,
                    booking_time = bookingTime,
                    status = "pending",
                    complaint = complaint,
                    createdAt = null,
                    updatedAt = null,
                    user = null,
                    vehicle = null,
                    service = null,
                    schedule = null
                )
                val result = bookingRepository.createBooking(newBooking)
                when (result) {
                    is Resource.Success -> {
                        _bookingResult.postValue(Event(Resource.Success("Booking berhasil dibuat!")))
                    }
                    is Resource.Error -> {
                        _bookingResult.postValue(Event(Resource.Error(result.message ?: "Gagal membuat booking.")))
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _bookingResult.postValue(Event(Resource.Error(e.message ?: "Terjadi kesalahan saat memproses booking.")))
            }
        }
    }
}