package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.communication.delivery.DeliveryUpdateStatusRequest
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.communication.task.TaskStatusUpdateRequest
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch

class DeliveryScheduleViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor
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

        val nextTask = getNextTask()
        if (nextTask == null)
            finishDelivery()
        else
            updateTaskStatus(nextTask, TaskStatus.IN_PROGRESS)
    }


    private fun finishDelivery() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    deliveryRepository.updateDeliveryStatus(
                        delivery!!.id,
                        prepareDeliveryUpdateStatusRequest()
                    )
                },
                onSuccess = { onDeliveryFinishSuccess() }
            )
        }
    }


    private fun onDeliveryFinishSuccess() {
        showToast(
            text = context.getString(R.string.delivery_schedule_finish_toast_message),
            toastLength = Toast.LENGTH_LONG
        )
    }


    private fun prepareDeliveryUpdateStatusRequest(): DeliveryUpdateStatusRequest {
        return DeliveryUpdateStatusRequest(DeliveryStatus.COMPLETED)
    }


    private fun getNextTask(): Task? {
        val tasks = delivery!!.tasks
        val nextTaskIndex = tasks.indexOf(selectedTask) + 1

        return tasks.getOrNull(nextTaskIndex)
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


    private fun fetchDeliverySchedule() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    val userId = loggedInUserAccessor.getLoggedInUser().id
                    _delivery.value =
                        deliveryRepository.getDeliveryByEmployeeIdAndDate(userId).body()
                }
            )
        }
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


    fun onStartButtonClick() {
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


    fun showStartButton(): Boolean {
        if (delivery == null)
            return false

        val tasks = delivery!!.tasks

        return tasks.none { it.status == TaskStatus.IN_PROGRESS } && tasks.any { it.status != TaskStatus.COMPLETED }
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
                onSuccess = { onTaskStatusUpdateSuccess(task, newStatus) }
            )
        }
    }


    private fun prepareTaskUpdateRequest(newStatus: TaskStatus): TaskStatusUpdateRequest {
        return TaskStatusUpdateRequest(status = newStatus)
    }


    private fun onTaskStatusUpdateSuccess(task: Task, newStatus: TaskStatus) {
        task.status = newStatus
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