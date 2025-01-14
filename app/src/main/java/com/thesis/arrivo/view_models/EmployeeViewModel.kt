package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.employee.EmployeeCreateAccountRequest
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.EmployeeUpdateRequest
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.mapError
import com.thesis.arrivo.utilities.showErrorDialog
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val repository: EmployeeRepository by lazy { EmployeeRepository() }

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

    private val _showEmployeeDetails = mutableStateOf(false)
    val showEmployeeDetails: Boolean get() = _showEmployeeDetails.value

    private val _clickedEmployee = mutableStateOf(Employee.emptyEmployee())
    var clickedEmployee = _clickedEmployee.value


    init {
        fetchEmployeesList()
    }


    fun toggleShowEmployeeDetails() {
        _showEmployeeDetails.value = !_showEmployeeDetails.value
    }


    private fun fetchEmployeesList() {
        viewModelScope.launch {
            try {
                loadingScreenManager.showLoadingScreen()
                val employeeList = repository.getAllEmployees()
                _employees.value = employeeList
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                loadingScreenManager.hideLoadingScreen()
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
                loadingScreenManager.showLoadingScreen()
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
                loadingScreenManager.hideLoadingScreen()
            }
        }
    }

    sealed class EmployeeAccountOperation {
        data class Create(val data: EmployeeCreateAccountRequest) : EmployeeAccountOperation()
        data class Update(val id: Long, val data: EmployeeUpdateRequest) :
            EmployeeAccountOperation()
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


    fun onSuccess(context: Context, editMode: Boolean) {
        showSuccessToast(context, editMode)
        navigationManager.navigateTo(NavigationItem.EmployeesAdmin, true)
    }


    fun onFailure(exception: Exception) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = mapError(exception, context)
        )
    }


    fun onFailure(errorResponse: ErrorResponse) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = errorResponse
        )
    }


    fun onEmployeeEditButtonClick(
        mainScaffoldViewModel: MainScaffoldViewModel,
    ) {
        setEmployeeToEdit(mainScaffoldViewModel = mainScaffoldViewModel)
        toggleShowEmployeeDetails()

        navigationManager.navigateTo(navigationItem = NavigationItem.EditEmployeeAdmin)
    }


    private fun setEmployeeToEdit(
        mainScaffoldViewModel: MainScaffoldViewModel,
    ) {
        mainScaffoldViewModel.employeeToEdit = clickedEmployee
    }


    fun onCreateEmployeeAccountButtonClick() {
        navigationManager.navigateTo(navigationItem = NavigationItem.CreateEmployeeAdmin)
    }


    fun onPlanADayButtonClick() {
        navigationManager.navigateTo(NavigationItem.PlanADayAdmin)
    }
}
