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
import androidx.navigation.NavHostController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ErrorResponse
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.mapError
import com.thesis.arrivo.utilities.navigateTo
import com.thesis.arrivo.utilities.showErrorDialog
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TasksListViewModel(
    private val navHostController: NavHostController,
    private val context: Context
) : ViewModel() {

    companion object {
        private var _selectedDate = mutableLongStateOf(getCurrentDateMillis())
        private val selectedDate: Long
            get() = _selectedDate.longValue

        private val _activeFilters = mutableStateListOf<TaskStatus>()
        private val activeFilters: List<TaskStatus>
            get() = _activeFilters

        private val RENAMED_FILTERS = mapOf(
            TaskStatus.COMPLETED to "Finished",
            TaskStatus.UNASSIGNED to "Free",
            TaskStatus.IN_PROGRESS to "Assigned",
        )

        fun getRenamedFilter(filer: TaskStatus) = RENAMED_FILTERS.getOrDefault(
            key = filer,
            defaultValue = filer.name
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
        filterTasks()
    }


    /**
     * Filters
     **/

    fun getActiveFilters() = activeFilters

    fun toggleFilterActive(taskFilter: TaskStatus) {
        if (activeFilters.contains(taskFilter))
            _activeFilters.remove(taskFilter)
        else
            _activeFilters.add(taskFilter)

        filterTasks()
    }


    /**
     * Add Task Button
     **/


    fun onAddTaskButtonClick() {
        navigateTo(
            navController = navHostController,
            navigationItem = NavigationItem.TaskCreateAdmin
        )
    }


    /**
     * Tasks List
     **/

    private val tasksRepository = TasksRepository()

    private val _tasksToShow = mutableStateListOf<Task>()
    val tasksToShow: List<Task>
        get() = _tasksToShow

    private val _allTasks = mutableStateListOf<Task>()
    var tasksFetchingInProgress by mutableStateOf(false)


    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                tasksFetchingInProgress = true
                _allTasks.clear()
                _allTasks.addAll(tasksRepository.getAllTasks())
                filterTasks()
            } catch (e: Exception) {
                onFailure(context, mapError(e, context))
            } finally {
                tasksFetchingInProgress = false
            }
        }
    }


    private fun filterTasks() {
        viewModelScope.launch {
            val filteredTasks = _allTasks.filter { task ->
                val matchesStatus = task.status in activeFilters || activeFilters.isEmpty()

                val matchesDate = task.assignedDate?.let {
                    val selectedDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(selectedDate), ZoneId.systemDefault())
                    task.assignedDate.toLocalDate() == selectedDateTime.toLocalDate()
                } ?: true

                matchesStatus && matchesDate
            }

            _tasksToShow.clear()
            _tasksToShow.addAll(filteredTasks)
        }
    }


    private fun onFailure(context: Context, error: ErrorResponse) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = error
        )
    }


    init {
        fetchTasks()
    }
}
