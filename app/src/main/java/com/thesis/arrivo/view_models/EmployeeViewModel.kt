package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.communication.employee.EmployeeCreateAccountRequest
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.communication.employee.EmployeeUpdateRequest
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.utilities.mapError
import com.thesis.arrivo.utilities.navigateTo
import com.thesis.arrivo.utilities.showErrorDialog
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel : ViewModel() {

    private val repository: EmployeeRepository by lazy { EmployeeRepository() }

    private val _employees = MutableStateFlow<List<EmployeeResponse>>(emptyList())
    val employees: StateFlow<List<EmployeeResponse>> = _employees.asStateFlow()

    private val _actionInProgress = mutableStateOf(false)
    val actionInProgress: Boolean
        get() = _actionInProgress.value

    private val _showEmployeeDetails = mutableStateOf(false)
    val showEmployeeDetails: Boolean get() = _showEmployeeDetails.value

    private val _clickedEmployee = mutableStateOf(EmployeeResponse.emptyEmployeeResponse())
    var clickedEmployee = _clickedEmployee.value


    fun toggleShowEmployeeDetails() {
        _showEmployeeDetails.value = !_showEmployeeDetails.value
    }


    private fun setActionInProgress(status: Boolean) {
        _actionInProgress.value = status
    }


    fun fetchEmployeesList(context: Context, onFailure: (ErrorResponse) -> Unit) {
        viewModelScope.launch {
            try {
                setActionInProgress(true)
                val employeeList = repository.getAllEmployees()
                _employees.value = employeeList
            } catch (e: Exception) {
                onFailure(mapError(e, context))
            } finally {
                setActionInProgress(false)
            }
        }
    }


    fun createEmployeeAccount(
        context: Context,
        createAccountRequest: EmployeeCreateAccountRequest,
        onSuccess: () -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                setActionInProgress(true)
                repository.createEmployeeAccount(createAccountRequest)
                onSuccess()
            } catch (e: Exception) {
                onFailure(mapError(e, context))
            } finally {
                setActionInProgress(false)
            }
        }
    }


    fun updateEmployeeAccount(
        context: Context,
        id: Long,
        updateAccountRequest: EmployeeUpdateRequest,
        onFailure: (ErrorResponse) -> Unit,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                setActionInProgress(true)
                repository.updateEmployeeAccount(id, updateAccountRequest)
                onSuccess()
            } catch (e: Exception) {
                onFailure(mapError(e, context))
            } finally {
                setActionInProgress(false)
            }
        }
    }


    private fun showAccountCreateSuccessToast(context: Context) {
        showToast(
            context = context,
            text = context.getString(R.string.create_account_success_message),
            toastLength = Toast.LENGTH_LONG
        )
    }


    fun onAccountCreateSuccess(context: Context, navController: NavHostController) {
        showAccountCreateSuccessToast(context)
        navigateTo(navController, NavigationItem.CreateEmployeeAdmin)
    }


    fun onAccountCreateFailure(context: Context, error: ErrorResponse) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = error
        )
    }


    fun setEmployeeToEdit(
        mainScaffoldViewModel: MainScaffoldViewModel,
    ) {
        mainScaffoldViewModel.employeeToEdit = clickedEmployee
    }

}
