package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.communication.employee.EmployeeCreateAccountRequest
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.communication.employee.EmployeeUpdateRequest
import com.thesis.arrivo.utilities.mapError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel() : ViewModel() {

    private val repository: EmployeeRepository by lazy { EmployeeRepository() }

    private val _employees = MutableStateFlow<List<EmployeeResponse>>(emptyList())
    val employees: StateFlow<List<EmployeeResponse>> = _employees.asStateFlow()

    private val _error = MutableStateFlow<ErrorResponse?>(null)
    val error: StateFlow<ErrorResponse?> = _error.asStateFlow()

    private val _fetchInProgress = mutableStateOf(false)
    val fetchInProgress: Boolean
        get() = _fetchInProgress.value


    private fun setFetchInProgress(status: Boolean) {
        _fetchInProgress.value = status
    }


    fun fetchEmployeesList() {
        getAllEmployees()
    }


    private fun getAllEmployees() {
        viewModelScope.launch {
            try {
                setFetchInProgress(true)
                val employeeList = repository.getAllEmployees()
                _employees.value = employeeList
                _error.value = null
            } catch (e: Exception) {
                _error.value = mapError(e)
                _error.value?.errors?.forEach { err ->
                    println(err)
                }
            } finally {
                setFetchInProgress(false)
            }
        }
    }


    fun createEmployeeAccount(createAccountRequest: EmployeeCreateAccountRequest) {
        viewModelScope.launch {
            try {
                setFetchInProgress(true)
                repository.createEmployeeAccount(createAccountRequest)
                _error.value = null
                getAllEmployees()
            } catch (e: Exception) {
                _error.value = mapError(e)
            } finally {
                setFetchInProgress(false)
            }
        }
    }


    fun updateEmployeeAccount(id: Long, updateAccountRequest: EmployeeUpdateRequest) {
        viewModelScope.launch {
            try {
                setFetchInProgress(true)
                repository.updateEmployeeAccount(id, updateAccountRequest)
                _error.value = null
                getAllEmployees()
            } catch (e: Exception) {
                _error.value = mapError(e)
            } finally {
                setFetchInProgress(false)
            }
        }
    }

}
