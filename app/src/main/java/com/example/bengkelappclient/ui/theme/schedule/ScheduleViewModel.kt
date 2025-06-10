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

    private val _updateResult = MutableLiveData<Resource<Unit>>()
    val updateResult: LiveData<Resource<Unit>> = _updateResult

    fun getSchedules() = viewModelScope.launch {
        _schedules.value = Resource.Loading()
        _schedules.value = repository.getSchedules()
    }

    fun updateSchedule(schedule: Schedule) = viewModelScope.launch {
        _updateResult.value = Resource.Loading()
        val result = repository.updateSchedule(schedule)
        _updateResult.value = result
        if (result is Resource.Success) {
            getSchedules() // Muat ulang daftar setelah pembaruan berhasil
        }
    }
}
