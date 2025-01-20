package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.Role
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager

class RoleViewModel(loadingScreenManager: LoadingScreenManager) : ViewModel() {

    private val serverRequestManager = ServerRequestManager(
        context = MainActivity.context,
        loadingScreenManager = loadingScreenManager
    )

    private val employeeRepository = EmployeeRepository()

    private lateinit var userRole: Role

    private val _roleFetchInProgress = mutableStateOf(true)
    val roleFetchInProgress: Boolean
        get() = _roleFetchInProgress.value


    suspend fun fetchUserRole() {
//        viewModelScope.launch {
        serverRequestManager.sendRequest(
            actionToPerform = { userRole = employeeRepository.getUserRole() },
            onSuccess = { onSuccess() },
            onFailure = { onFailure() }
        )
//        }
    }


    private fun onSuccess() {
        _roleFetchInProgress.value = false
    }


    private fun onFailure() {
        MainScaffoldViewModel.reset()
    }


    fun isAdmin(): Boolean {
        return userRole == Role.ADMIN
    }

}