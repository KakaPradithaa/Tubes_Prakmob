package com.example.bengkelappclient.ui.theme.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.BookingDetails
import com.example.bengkelappclient.data.repository.BookingRepository
import com.example.bengkelappclient.util.Resource // DIUBAH: Import Resource, bukan ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    // DIUBAH: Gunakan Resource<T> untuk konsistensi dengan ViewModel lain
    private val _bookingHistory = MutableLiveData<Resource<List<BookingDetails>>>()
    val bookingHistory: LiveData<Resource<List<BookingDetails>>> = _bookingHistory

    // Panggil fungsi ini dari Activity/Fragment untuk memulai pengambilan data
    fun fetchBookingHistory() {
        viewModelScope.launch {
            _bookingHistory.value = Resource.Loading() // Kirim status Loading ke UI

            // DIUBAH: Panggil fungsi repository yang baru dan lebih efisien
            val result = bookingRepository.getBookingDetails()

            // Logika ini sudah benar, hanya perlu mengubah ServiceResult menjadi Resource
            result.fold(
                onSuccess = { bookingDetailsList ->
                    // Jika sukses, kirim Resource.Success ke UI
                    if (bookingDetailsList.isEmpty()) {
                        _bookingHistory.value = Resource.Error("Riwayat booking tidak ditemukan.")
                    } else {
                        _bookingHistory.value = Resource.Success(bookingDetailsList)
                    }
                },
                onFailure = { exception ->
                    // Jika gagal, kirim Resource.Error ke UI
                    _bookingHistory.value = Resource.Error(exception.message ?: "Gagal memuat riwayat booking")
                }
            )
        }
    }
}