package com.example.bengkelappclient.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Schedule
import com.example.bengkelappclient.data.repository.ScheduleRepository
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _schedules = MutableLiveData<Resource<List<Schedule>>>()
    val schedules: LiveData<Resource<List<Schedule>>> = _schedules

    // Diubah: Menggunakan Resource<Schedule> agar bisa mendapat feedback objek yang ter-update
    private val _updateResult = MutableLiveData<Resource<Schedule>>()
    val updateResult: LiveData<Resource<Schedule>> = _updateResult

    fun getSchedules() = viewModelScope.launch {
        _schedules.value = Resource.Loading()
        _schedules.value = repository.getSchedules()
    }

    /**
     * Fungsi ini sekarang mengirim dua argumen yang benar ke repository.
     */
    fun updateSchedule(schedule: Schedule) = viewModelScope.launch {
        _updateResult.value = Resource.Loading()

        // PERBAIKAN DI SINI: Kirim schedule.id dan schedule
        val result = repository.updateSchedule(schedule.id, schedule)

        _updateResult.value = result

        // Muat ulang daftar jika pembaruan berhasil
        if (result is Resource.Success) {
            getSchedules()
        }
    }
}