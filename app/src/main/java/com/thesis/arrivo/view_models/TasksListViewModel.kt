package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.communication.task.TaskToEdit
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.SelectedDateManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import kotlinx.coroutines.launch

class TasksListViewModel(
    context: Context,
    private val mainScaffoldViewModel: MainScaffoldViewModel,
    private val loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
) : ViewModel(), LoadingScreenStatusChecker {
    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    companion object {
        private val selectedDateManager: SelectedDateManager = SelectedDateManager()

        private val _activeFilters = mutableStateListOf(TaskStatus.UNASSIGNED)
        private val activeFilters: List<TaskStatus>
            get() = _activeFilters


        fun getRenamedFilter(filer: TaskStatus): String {
            return when (filer) {
                TaskStatus.UNASSIGNED -> "Free"
                TaskStatus.COMPLETED -> "Finished"
                TaskStatus.IN_PROGRESS -> "In Delivery"
                TaskStatus.ASSIGNED -> "Assigned"
            }
        }


        fun getFilterColor(filer: TaskStatus): Color {
            return when (filer) {
                TaskStatus.COMPLETED -> Settings.TASK_FINISHED_COLOR
                TaskStatus.UNASSIGNED -> Settings.TASK_FREE_COLOR
                TaskStatus.ASSIGNED -> Settings.TASK_ASSIGNED_COLOR
                TaskStatus.IN_PROGRESS -> Settings.TASK_IN_PROGRESS_COLOR
            }
        }
    }

    /**
     * Date picker
     **/


    fun getSelectedDate(): Long = selectedDateManager.selectedDate


    fun onDateSelected(dateMillis: Long?) {
        selectedDateManager.selectedDate = dateMillis ?: getCurrentDateMillis()
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
        navigationManager.navigateTo(routeOrItem = NavigationItem.TaskCreateAdmin)
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
        navigationManager.navigateTo(routeOrItem = NavigationItem.TaskEditAdmin)
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
                    task.assignedDate.toEpochDay() == selectedDateManager.localDate.toEpochDay()
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

    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }
}
