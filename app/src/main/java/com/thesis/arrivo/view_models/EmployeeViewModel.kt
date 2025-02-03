package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.employee.EmployeeCreateAccountRequest
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.employee.EmployeeStatus
import com.thesis.arrivo.communication.employee.EmployeeUpdateRequest
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(
    private val context: Context,
    loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
    private val mainViewModel: MainViewModel,
) : ViewModel() {
    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

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
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _employees.value = repository.getAllEmployees()
                }
            )
        }
    }


    fun onCreateOrEditButtonClick(
        authViewModel: AuthViewModel,
        editMode: Boolean
    ) {
        val request = if (editMode) {
            EmployeeAccountOperation.Update(
                id = mainViewModel.employeeToEdit.id,
                data = authViewModel.prepareEmployeeUpdateRequest()
            )
        } else {
            EmployeeAccountOperation.Create(
                data = authViewModel.prepareEmployeeCreateRequest()
            )
        }

        handleEmployeeAccountOperation(operation = request, editMode = editMode)
    }

    private fun handleEmployeeAccountOperation(
        operation: EmployeeAccountOperation,
        editMode: Boolean
    ) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    when (operation) {
                        is EmployeeAccountOperation.Create -> {
                            repository.createEmployeeAccount(operation.data)
                        }

                        is EmployeeAccountOperation.Update -> {
                            repository.updateEmployeeAccount(operation.id, operation.data)
                        }
                    }
                },
                onSuccess = { onSuccess(editMode) }
            )
        }
    }

    sealed class EmployeeAccountOperation {
        data class Create(val data: EmployeeCreateAccountRequest) : EmployeeAccountOperation()
        data class Update(val id: Long, val data: EmployeeUpdateRequest) :
            EmployeeAccountOperation()
    }


    private fun showSuccessToast(editMode: Boolean) {
        val messageResId = if (editMode) {
            R.string.employee_edit_success_message
        } else {
            R.string.create_account_success_message
        }

        showToast(
            text = context.getString(messageResId),
            toastLength = Toast.LENGTH_LONG
        )
    }


    private fun onSuccess(editMode: Boolean) {
        showSuccessToast(editMode)
        navigationManager.navigateTo(NavigationItem.EmployeesListAdmin, true)
    }


    fun onEmployeeEditButtonClick() {
        mainViewModel.employeeToEdit = clickedEmployee
        toggleShowEmployeeDetails()

        navigationManager.navigateTo(routeOrItem = NavigationItem.EditEmployeeAdmin)
    }


    fun onCreateEmployeeAccountButtonClick() {
        navigationManager.navigateTo(routeOrItem = NavigationItem.CreateEmployeeAdmin)
    }


    fun getEmployeeToEditStatus(): EmployeeStatus {
        return mainViewModel.employeeToEdit.status
    }


    /**
     * Other
     **/


    fun onWorkTimeAnalysisButtonClick() {
        navigationManager.navigateTo(routeOrItem = NavigationItem.WorkTimeAdmin)
    }
}
