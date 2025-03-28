package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
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
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.SelectedDateManager
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.localDateToMillis
import com.thesis.arrivo.utilities.showDefaultErrorDialog
import kotlinx.coroutines.launch
import java.time.LocalDate

class DeliveryOptionsViewModel(
    val editMode: Boolean,
    private val context: Context,
    private val navigationManager: NavigationManager,
    private val loadingScreenManager: LoadingScreenManager,
    private val deliverySharedViewModel: DeliverySharedViewModel,
) : ViewModel(), LoadingScreenStatusChecker {

    companion object {
        private val selectedDateManager: SelectedDateManager = SelectedDateManager()
    }

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)


    /**
     * Date Picker
     **/


    private val _isDatePickerError = mutableStateOf(false)
    val isDatePickerError: Boolean
        get() = _isDatePickerError.value


    fun getSelectedDate(): Long = selectedDateManager.selectedDate


    fun onDateSelected(dateMillis: Long?) {
        selectedDateManager.selectedDate = dateMillis ?: getCurrentDateMillis()
        _isDatePickerError.value = false

        fetchUnassignedEmployeesOnDate()

        if (editMode) {
            _availableTasks.addAll(prevCheckedTasks.filter { prevCheckedTask ->
                prevCheckedTask.id !in _availableTasks.map { it.id }
            })
        }
    }


    private fun isSelectedDateEditDate(): Boolean {
        return selectedDateManager.localDate.toEpochDay() == editDate.toEpochDay()
    }


    /**
     * Employee Selector
     **/


    private var editModeFirstEmpSet = false

    var selectedEmployee by mutableStateOf(Employee.emptyEmployee())
    var employeeSpinnerError by mutableStateOf(false)
    private var isEmployeeSelected by mutableStateOf(false)

    private val _employeesList = mutableStateListOf<Employee>()
    val employeesList: List<Employee>
        get() = _employeesList

    private val employeesRepository = EmployeeRepository()


    private fun fetchUnassignedEmployeesOnDate() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _employeesList.clear()
                    _employeesList.addAll(
                        employeesRepository.getUnassignedEmployeesOnDate(selectedDateManager.localDate)
                    )
                },
                onSuccess = { onEmployeesFetchSuccess() }
            )
        }
    }


    private fun shouldSelectedEmpBeSelected(): Boolean {
        return _employeesList.find { employee -> employee.id == selectedEmployee.id } != null
    }


    private fun onEmployeesFetchSuccess() {
        if (editMode && isSelectedDateEditDate()) {
            _employeesList.add(deliverySharedViewModel.employee)

            if (!editModeFirstEmpSet) {
                selectedEmployee = deliverySharedViewModel.employee
                isEmployeeSelected = true
                editModeFirstEmpSet = true
            }
        }

        if (!shouldSelectedEmpBeSelected()) {
            selectedEmployee = Employee.emptyEmployee()
            isEmployeeSelected = false
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
                },
                onSuccess = { onTaskFetchSuccess() }
            )
        }
    }


    private fun onTaskFetchSuccess() {
        if (!editMode)
            return

        checkedTasks.addAll(prevCheckedTasks)
        sortTasksList()
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

        deliverySharedViewModel.selectedTasks.clear()
        deliverySharedViewModel.selectedTasks.addAll(checkedTasks)

        deliverySharedViewModel.selectedDate = selectedDateManager.localDate
        deliverySharedViewModel.employee = selectedEmployee

        navigationManager.navigateTo(NavigationItem.DeliveryConfirmAdmin)
    }


    private fun validateConditions(): Boolean {
        if (!isEmployeeSelected) {
            employeeSpinnerError = true
            return false
        }

        if (selectedDateManager.localDate.isBefore(LocalDate.now())) {
            _isDatePickerError.value = true
            return false
        }

        if (checkedTasks.isEmpty()) {
            showDefaultErrorDialog(
                context = context,
                title = context.getString(R.string.error_title),
                message = context.getString(R.string.delivery_no_task_selected_error_message)
            )

            return false
        }

        return true
    }


    /**
     * Edit Mode
     **/


    private lateinit var editDate: LocalDate
    private lateinit var editedEmployee: Employee
    private val prevCheckedTasks: MutableList<Task> = mutableListOf()


    /**
     * Initializer
     **/


    init {
        deliverySharedViewModel.editMode = editMode
        selectedDateManager.selectedDate = getCurrentDateMillis()

        fetchFreeTasks()

        if (!editMode)
            fetchUnassignedEmployeesOnDate()
        else {
            editDate = deliverySharedViewModel.selectedDate
            editedEmployee = deliverySharedViewModel.employee
            prevCheckedTasks.addAll(deliverySharedViewModel.selectedTasks)

            onDateSelected(localDateToMillis(editDate))
        }
    }

    override fun isLoadingScreenEnabled(): Boolean = loadingScreenManager.isLoadingScreenEnabled()
}