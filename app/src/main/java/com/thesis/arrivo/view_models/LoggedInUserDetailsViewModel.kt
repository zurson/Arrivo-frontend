package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.Role
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager

class LoggedInUserDetailsViewModel(context: Context, loadingScreenManager: LoadingScreenManager) :
    ViewModel() {

    private val serverRequestManager = ServerRequestManager(
        context = context,
        loadingScreenManager = loadingScreenManager
    )

    private val employeeRepository = EmployeeRepository()
    private var loggedInUser by mutableStateOf(Employee.emptyEmployee())


    suspend fun fetch(callback: (Boolean) -> Unit) {
        serverRequestManager.sendRequest(
            actionToPerform = { loggedInUser = employeeRepository.getLoggedInUserDetails() },
            onFailure = { callback(false) },
            onSuccess = { callback(true) },
            showOnFailureDefaultInfoBox = false
        )
    }


    fun isAdmin(): Boolean {
        return loggedInUser.role == Role.ADMIN
    }


    fun getUser(): Employee = loggedInUser

}