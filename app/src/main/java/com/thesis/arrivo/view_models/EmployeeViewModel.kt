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


    fun onCreateOrEditButtonClick(
        context: Context,
        mainScaffoldViewModel: MainScaffoldViewModel,
        authViewModel: AuthViewModel,
        onSuccess: () -> Unit,
        onFailure: (ErrorResponse) -> Unit,
        editMode: Boolean
    ) {
        val request = if (editMode) {
            EmployeeAccountOperation.Update(
                id = mainScaffoldViewModel.employeeToEdit.id,
                data = authViewModel.prepareEmployeeUpdateRequest()
            )
        } else {
            EmployeeAccountOperation.Create(
                data = authViewModel.prepareEmployeeCreateRequest()
            )
        }

        handleEmployeeAccountOperation(
            context = context,
            operation = request,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    private fun handleEmployeeAccountOperation(
        context: Context,
        operation: EmployeeAccountOperation,
        onSuccess: () -> Unit,
        onFailure: (ErrorResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                setActionInProgress(true)
                when (operation) {
                    is EmployeeAccountOperation.Create -> {
                        repository.createEmployeeAccount(operation.data)
                    }
                    is EmployeeAccountOperation.Update -> {
                        repository.updateEmployeeAccount(operation.id, operation.data)
                    }
                }
                onSuccess()
            } catch (e: Exception) {
                onFailure(mapError(e, context))
            } finally {
                setActionInProgress(false)
            }
        }
    }

    sealed class EmployeeAccountOperation {
        data class Create(val data: EmployeeCreateAccountRequest) : EmployeeAccountOperation()
        data class Update(val id: Long, val data: EmployeeUpdateRequest) : EmployeeAccountOperation()
    }



    private fun showSuccessToast(context: Context, editMode: Boolean) {
        val messageResId = if (editMode) {
            R.string.employee_edit_success_message
        } else {
            R.string.create_account_success_message
        }

        showToast(
            context = context,
            text = context.getString(messageResId),
            toastLength = Toast.LENGTH_LONG
        )
    }


    fun onSuccess(context: Context, navController: NavHostController, editMode: Boolean) {
        showSuccessToast(context, editMode)
        navigateTo(navController, NavigationItem.EmployeesAdmin)
    }


    fun onFailure(context: Context, error: ErrorResponse) {
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
