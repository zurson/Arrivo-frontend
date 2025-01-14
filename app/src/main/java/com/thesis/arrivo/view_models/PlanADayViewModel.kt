package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.convertLongToLocalDate
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlanADayViewModel(
    context: Context,
    loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager
) : ViewModel() {

    companion object {
        private var _selectedDate = mutableLongStateOf(getCurrentDateMillis())
        private val selectedDate: Long
            get() = _selectedDate.longValue
    }

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    /**
     * Date Picker
     **/


    private var selectedLocalDate: LocalDate = convertLongToLocalDate(selectedDate)


    fun getSelectedDate(): Long = selectedDate


    fun onDateSelected(dateMillis: Long?) {
        _selectedDate.longValue = dateMillis ?: getCurrentDateMillis()
        selectedLocalDate = convertLongToLocalDate(selectedDate)
    }


    /**
     * Employee Selector
     **/

    var selectedEmployee by mutableStateOf(Employee.emptyEmployee())
    val employeeSpinnerError by mutableStateOf(false)
    private var isEmployeeSelected by mutableStateOf(false)

    private val _employeesList = mutableStateListOf<Employee>()
    val employeesList: List<Employee>
        get() = _employeesList

    private val employeesRepository = EmployeeRepository()


    private fun fetchEmployeesList() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _employeesList.clear()
                    _employeesList.addAll(employeesRepository.getAllEmployees())
                },
            )
        }
    }


    fun employeeToString(emp: Employee): String {
        return "${capitalize(emp.firstName.lowercase())} ${capitalize(emp.lastName.lowercase())}"
    }


    fun onEmployeeSelected(emp: Employee) {
        selectedEmployee = emp
        isEmployeeSelected = true
    }

    init {
        fetchEmployeesList()
    }

}