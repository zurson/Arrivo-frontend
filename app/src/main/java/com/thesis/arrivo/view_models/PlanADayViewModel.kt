package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.employee.EmployeeRepository
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.convertLongToLocalDate
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.showDefaultErrorDialog
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlanADayViewModel(
    private val context: Context,
    private val navigationManager: NavigationManager,
    loadingScreenManager: LoadingScreenManager
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
    var employeeSpinnerError by mutableStateOf(false)
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
        employeeSpinnerError = false
    }


    /**
     * Available Tasks
     **/


    private val tasksRepository = TasksRepository()

    private val _availableTasks = mutableStateListOf<Task>()
    val availableTasks: List<Task>
        get() = _availableTasks


    private fun fetchFreeTasks() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _availableTasks.clear()
                    _availableTasks.addAll(tasksRepository.getFreeTasks())
                }
            )
        }
    }


    /**
     * Task Details
     **/


    private val _selectedTask = mutableStateOf(Task.emptyTask())
    val selectedTask: Task
        get() = _selectedTask.value

    private val _showTaskDetailsDialog = mutableStateOf(false)
    val showTaskDetailsDialog: Boolean
        get() = _showTaskDetailsDialog.value


    private fun toggleShowTaskDetailsDialog() {
        _showTaskDetailsDialog.value = !_showTaskDetailsDialog.value
    }


    fun onTaskDialogDismiss() {
        toggleShowTaskDetailsDialog()
    }


    fun onTaskSelected(task: Task) {
        _selectedTask.value = task
        toggleShowTaskDetailsDialog()
    }


    /**
     * Checked Tasks
     **/


    private val checkedTasks = mutableStateListOf<Task>()

    fun isTaskChecked(task: Task): Boolean = checkedTasks.contains(task)


    fun onTaskCheckedChange(task: Task) {
        if (checkedTasks.contains(task))
            checkedTasks.remove(task)
        else
            checkedTasks.add(task)

        sortTasksList()
    }


    private fun sortTasksList() {
        checkedTasks.forEach { checked ->
            _availableTasks.remove(checked)
            _availableTasks.add(0, checked)
        }
    }


    /**
     * Button - NEXT
     **/


    fun onButtonNextClick() {
        if (!validateConditions())
            return

    }


    private fun validateConditions(): Boolean {
        if (!isEmployeeSelected) {
            employeeSpinnerError = true
            return false
        }

        if (checkedTasks.isEmpty()) {
            showDefaultErrorDialog(
                context = context,
                title = context.getString(R.string.error_title),
                message = context.getString(R.string.plan_a_day_no_task_selected_error_message)
            )

            return false
        }

        return true
    }


    /**
     * Initializer
     **/


    init {
        fetchFreeTasks()
        fetchEmployeesList()
    }
}