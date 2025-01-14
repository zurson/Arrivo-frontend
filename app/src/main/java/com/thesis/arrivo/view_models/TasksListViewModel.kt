package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.TaskToEdit
import com.thesis.arrivo.utilities.Location
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.convertLongToLocalDate
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import kotlinx.coroutines.launch
import java.time.LocalDate

class TasksListViewModel(
    context: Context,
    private val mainScaffoldViewModel: MainScaffoldViewModel,
    loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
) : ViewModel() {
    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    companion object {
        private var _selectedDate = mutableLongStateOf(getCurrentDateMillis())
        private val selectedDate: Long
            get() = _selectedDate.longValue

        private val _activeFilters = mutableStateListOf(TaskStatus.UNASSIGNED)
        private val activeFilters: List<TaskStatus>
            get() = _activeFilters

        private val RENAMED_FILTERS = mapOf(
            TaskStatus.COMPLETED to "Finished",
            TaskStatus.UNASSIGNED to "Free",
            TaskStatus.IN_PROGRESS to "Assigned",
        )

        fun getRenamedFilter(filer: TaskStatus) = RENAMED_FILTERS.getOrDefault(
            key = filer, defaultValue = filer.name
        )


        fun getFilterColor(filer: TaskStatus): Color {
            return when (filer) {
                TaskStatus.COMPLETED -> Settings.TASK_FINISHED_COLOR
                TaskStatus.UNASSIGNED -> Settings.TASK_FREE_COLOR
                TaskStatus.IN_PROGRESS -> Settings.TASK_ASSIGNED_COLOR
            }
        }
    }

    /**
     * Date picker
     **/


    fun getSelectedDate(): Long = selectedDate


    fun onDateSelected(dateMillis: Long?) {
        _selectedDate.longValue = dateMillis ?: getCurrentDateMillis()
        selectedLocalDate = convertLongToLocalDate(selectedDate)
        filterTasks()
    }


    /**
     * Filters
     **/

    fun getActiveFilters() = activeFilters

    fun toggleFilterActive(taskFilter: TaskStatus) {
        if (activeFilters.contains(taskFilter)) _activeFilters.remove(taskFilter)
        else _activeFilters.add(taskFilter)

        filterTasks()
    }


    /**
     * Add Task Button
     **/


    fun onAddTaskButtonClick() {
        navigationManager.navigateTo(navigationItem = NavigationItem.TaskCreateAdmin)
    }


    /**
     * Task details
     **/


    var showTaskDetailsDialog by mutableStateOf(false)
    private val _selectedTask = mutableStateOf(Task.emptyTask())
    val selectedTask: Task
        get() = _selectedTask.value


    fun onTaskSelected(task: Task) {
        _selectedTask.value = task
        setTaskToEdit(task)
        toggleShowTaskDetailsDialog()
    }


    fun onTaskEditButtonClick() {
        toggleShowTaskDetailsDialog()
        navigationManager.navigateTo(navigationItem = NavigationItem.TaskEditAdmin)
    }


    private fun setTaskToEdit(task: Task) {
        mainScaffoldViewModel.taskToEdit = TaskToEdit(
            task = task,
            address = task.addressText,
            location = Location(task.location.latitude, task.location.longitude)
        )
    }


    fun onTaskDismiss() {
        toggleShowTaskDetailsDialog()
    }


    private fun toggleShowTaskDetailsDialog() {
        showTaskDetailsDialog = !showTaskDetailsDialog
    }


    /**
     * Tasks List
     **/

    private val tasksRepository = TasksRepository()

    private var selectedLocalDate: LocalDate = convertLongToLocalDate(selectedDate)

    private val _tasksToShow = mutableStateListOf<Task>()
    val tasksToShow: List<Task>
        get() = _tasksToShow

    private val _allTasks = mutableStateListOf<Task>()


    private fun fetchTasks() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _allTasks.clear()
                    _allTasks.addAll(tasksRepository.getAllTasks())
                },
                onSuccess = { filterTasks() }
            )
        }
    }


    private fun filterTasks() {
        viewModelScope.launch {
            val filteredTasks = _allTasks.filter { task ->
                val matchesStatus = task.status in activeFilters || activeFilters.isEmpty()

                val matchesDate = task.assignedDate?.let {
                    task.assignedDate.toLocalDate().toEpochDay() == selectedLocalDate.toEpochDay()
                } ?: true

                matchesStatus && matchesDate
            }

            _tasksToShow.clear()
            _tasksToShow.addAll(filteredTasks)
        }
    }


    init {
        fetchTasks()
    }
}
