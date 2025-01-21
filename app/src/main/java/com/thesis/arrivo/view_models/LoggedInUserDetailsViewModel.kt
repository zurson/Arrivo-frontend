package com.thesis.arrivo.view_models

import android.content.Context
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
    private var loggedUserDetails: Employee? = null


    suspend fun fetch(callback: (Boolean) -> Unit) {
        serverRequestManager.sendRequest(
            actionToPerform = { loggedUserDetails = employeeRepository.getLoggedInUserDetails() },
            onFailure = { callback(false) },
            onSuccess = { callback(true) },
            showOnFailureDefaultInfoBox = false
        )
    }


    fun isAdmin(): Boolean {
        if (loggedUserDetails == null)
            return false

        return loggedUserDetails!!.role == Role.ADMIN
    }


    fun getLoggedUserDetails(): Employee = loggedUserDetails!!

}