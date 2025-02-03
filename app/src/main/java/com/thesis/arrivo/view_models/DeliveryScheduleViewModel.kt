package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.communication.task.TaskStatusUpdateRequest
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.utilities.BreakManager
import com.thesis.arrivo.utilities.Settings.Companion.BREAK_BEFORE_NOTIFICATION_IN_SECONDS
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.utilities.notifications.Notifier
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DeliveryScheduleViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModel(), LoadingScreenStatusChecker {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)


    /**
     * Task Details
     **/

    private val _showMarkAsFinishedConfirmationDialog = mutableStateOf(false)
    val showMarkAsFinishedConfirmationDialog: Boolean
        get() = _showMarkAsFinishedConfirmationDialog.value


    private val _showTaskDetailsDialog = mutableStateOf(false)
    val showTaskDetailsDialog: Boolean
        get() = _showTaskDetailsDialog.value

    private val _selectedTask = mutableStateOf(Task.emptyTask())
    val selectedTask: Task
        get() = _selectedTask.value


    fun toggleShowTaskDetailsDialog() {
        _showTaskDetailsDialog.value = !_showTaskDetailsDialog.value
    }


    fun toggleShowMarkAsFinishedConfirmationDialog() {
        _showMarkAsFinishedConfirmationDialog.value = !_showMarkAsFinishedConfirmationDialog.value
    }


    fun onMarkAsFinishedConfirmationYesClick() {
        toggleShowMarkAsFinishedConfirmationDialog()

        updateTaskStatus(selectedTask, TaskStatus.COMPLETED)
    }


    fun onMarkAsFinishedConfirmationNoClick() {
        toggleShowMarkAsFinishedConfirmationDialog()
    }


    fun onMarkAsFinishedConfirmationDismiss() {
        toggleShowMarkAsFinishedConfirmationDialog()
    }


    fun onTaskSelected(task: Task) {
        _selectedTask.value = task
        toggleShowTaskDetailsDialog()
    }


    fun onTaskDetailsDialogDismiss() {
        toggleShowTaskDetailsDialog()
    }


    fun onTaskDetailsDialogButtonClick() {
        if (selectedTask.status == TaskStatus.IN_PROGRESS)
            toggleShowMarkAsFinishedConfirmationDialog()

        toggleShowTaskDetailsDialog()
    }


    fun detailsDialogButtonText(): String {
        val id = if (selectedTask.status == TaskStatus.IN_PROGRESS)
            R.string.delivery_schedule_details_dialog_finish_button_text
        else
            R.string.delivery_schedule_details_dialog_dismiss_button_text

        return context.getString(id)
    }


    /**
     * Schedule Fetching
     **/


    val deliveryRepository = DeliveryRepository()

    private val _delivery = mutableStateOf<Delivery?>(null)
    val delivery: Delivery?
        get() = _delivery.value

    private val _activeTask = mutableStateOf<Task?>(null)
    val activeTask: Task?
        get() = _activeTask.value


    private fun fetchDeliverySchedule() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    val userId = loggedInUserAccessor.getLoggedInUser().id
                    _delivery.value =
                        deliveryRepository.getDeliveryByEmployeeIdAndDate(userId).body()
                },
                onSuccess = { onDeliveryScheduleFetchSuccess() },
                onFailure = { onDeliveryScheduleFetchFailure() }
            )
        }
    }


    private fun onDeliveryScheduleFetchSuccess() {
        if (delivery == null)
            return

        mapSharedViewModel.startTime = delivery!!.startDate
        mapSharedViewModel.breakTime = delivery!!.breakDate
        mapSharedViewModel.deliveryId = delivery!!.id

        val started = delivery!!.status == DeliveryStatus.IN_PROGRESS
        if (!started)
            return

        val taskInProgress = delivery!!.tasks.first { it.status == TaskStatus.IN_PROGRESS }
        mapSharedViewModel.destination = taskInProgress.location
        firstUpdateProceeded = true

        _activeTask.value = taskInProgress
    }


    private fun onDeliveryScheduleFetchFailure() {
        mapSharedViewModel.startTime = null
        mapSharedViewModel.breakTime = null
    }


    /**
     * Start Button
     **/

    private val taskRepository = TasksRepository()

    private val _showStartConfirmationDialog = mutableStateOf(false)
    val showStartConfirmationDialog: Boolean
        get() = _showStartConfirmationDialog.value


    private fun toggleShowStartConfirmationDialog() {
        _showStartConfirmationDialog.value = !_showStartConfirmationDialog.value
    }

    private var firstUpdateProceeded = false


    fun onStartButtonClick() {
        if (!isStartButtonActive())
            return

        toggleShowStartConfirmationDialog()
    }


    fun onStartConfirmationYesClick() {
        toggleShowStartConfirmationDialog()
        startDelivery()
    }


    fun onStartConfirmationNoClick() {
        toggleShowStartConfirmationDialog()
    }


    fun onStartConfirmationDismiss() {
        toggleShowStartConfirmationDialog()
    }


    fun isStartButtonActive(): Boolean {
        if (delivery == null)
            return false

        if (delivery!!.status != DeliveryStatus.ASSIGNED)
            return false

        val tasks = delivery!!.tasks

        return tasks.none { it.status == TaskStatus.IN_PROGRESS } &&
                tasks.any { it.status != TaskStatus.COMPLETED } &&
                !loadingScreenManager.isLoadingScreenEnabled()
    }


    private fun startDelivery() {
        if (delivery == null)
            return

        val firstTask = delivery!!.tasks.first()
        updateTaskStatus(firstTask, TaskStatus.IN_PROGRESS)
    }


    private fun updateTaskStatus(task: Task, newStatus: TaskStatus) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    taskRepository.updateTaskStatus(
                        id = task.id,
                        statusUpdateRequest = prepareTaskUpdateRequest(newStatus)
                    )
                },
                onSuccess = { onTaskStatusUpdateSuccess(task, newStatus) },
                onFailure = { onTaskStatusUpdateFailure() }
            )
        }
    }


    private fun isFirstTask(task: Task): Boolean {
        return delivery!!.tasks.first().id == task.id
    }


    private fun prepareTaskUpdateRequest(newStatus: TaskStatus): TaskStatusUpdateRequest {
        return TaskStatusUpdateRequest(status = newStatus)
    }


    private fun scheduleBreakNotifications(breakStartTime: LocalDateTime) {
        val title = context.getString(R.string.break_notification_title)

        Notifier.scheduleNotificationAt(
            title = title,
            message = context.getString(R.string.break_notification_before_message),
            dateTime = breakStartTime.minusSeconds(BREAK_BEFORE_NOTIFICATION_IN_SECONDS)
        )

        Notifier.scheduleNotificationAt(
            title = title,
            message = context.getString(R.string.break_notification_now_message),
            dateTime = breakStartTime
        )
    }


    private fun onTaskStatusUpdateFailure() {
        mapSharedViewModel.destination = null
    }


    private fun onTaskStatusUpdateSuccess(task: Task, newStatus: TaskStatus) {
        task.status = newStatus

        if (isFirstTask(task) && !firstUpdateProceeded) {
            mapSharedViewModel.destination = task.location
            firstUpdateProceeded = true
            _activeTask.value = task

            val startTime = LocalDateTime.now()

            mapSharedViewModel.startTime = startTime
            mapSharedViewModel.breakTime = null

            val breakStartTime = BreakManager.getBreakStartTime(startTime)
            scheduleBreakNotifications(breakStartTime)

            return
        }

        val nextTask = getNextTask()
        _activeTask.value = nextTask

        if (nextTask != null) {
            updateTaskStatus(nextTask, TaskStatus.IN_PROGRESS)
            mapSharedViewModel.destination = nextTask.location
        } else {
            mapSharedViewModel.destination = null
        }
    }


    private fun getNextTask(): Task? {
        val tasks = delivery!!.tasks
        val nextTaskIndex = tasks.indexOf(selectedTask) + 1

        val nextTask = tasks.getOrNull(nextTaskIndex)

        return nextTask
    }


    /**
     * Initializer
     **/


    init {
        fetchDeliverySchedule()
    }


    /**
     * Interface
     **/


    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }

}