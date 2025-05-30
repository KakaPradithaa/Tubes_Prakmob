// ui/customer/CustomerViewModel.kt
package com.example.bengkelappclient.ui.theme.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bengkelappclient.data.model.Customer
import com.example.bengkelappclient.data.repository.CustomerRepository
import com.example.bengkelappclient.util.Event
import com.example.bengkelappclient.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    val customers: LiveData<List<Customer>> = customerRepository.allCustomers // Dari Room

    private val _networkState = MutableLiveData<Event<Resource<Unit>>>()
    val networkState: LiveData<Event<Resource<Unit>>> = _networkState

    private val _addCustomerResult = MutableLiveData<Event<Resource<Customer>>>()
    val addCustomerResult: LiveData<Event<Resource<Customer>>> = _addCustomerResult

    init {
        refreshCustomers() // Panggil data dari API saat ViewModel dibuat
    }

    fun refreshCustomers() {
        viewModelScope.launch {
            _networkState.value = Event(Resource.Loading())
            val result = customerRepository.refreshCustomersFromApi()
            result.fold(
                onSuccess = { _networkState.value = Event(Resource.Success(Unit)) },
                onFailure = { _networkState.value = Event(Resource.Error(it.message ?: "Failed to refresh")) }
            )
        }
    }

    fun addCustomer(name: String, phone: String, address: String?) {
        viewModelScope.launch {
            _addCustomerResult.value = Event(Resource.Loading())
            val result = customerRepository.addCustomer(name, phone, address)
            result.fold(
                onSuccess = { customer -> _addCustomerResult.value = Event(Resource.Success(customer)) },
                onFailure = { _addCustomerResult.value = Event(Resource.Error(it.message ?: "Failed to add customer")) }
            )
        }
    }
}