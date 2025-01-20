package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.DeliveryCreateRequest
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.communication.delivery.DeliveryTask
import com.thesis.arrivo.communication.delivery.DeliveryUpdateRequest
import com.thesis.arrivo.communication.delivery.OptimizeRoutesRequest
import com.thesis.arrivo.communication.delivery.TaskToOptimize
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_TIME_EXCEEDED_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_TIME_OK_COLOR
import com.thesis.arrivo.utilities.exceptions.DataCorruptedException
import com.thesis.arrivo.utilities.exceptions.OptimizationFailedException
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch
import java.util.LinkedList

class DeliveryConfirmViewModel(
    private val context: Context,
    private val navigationManager: NavigationManager,
    private val loadingScreenManager: LoadingScreenManager,
    val deliverySharedViewModel: DeliverySharedViewModel
) : ViewModel(), LoadingScreenStatusChecker {
    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    /**
     * Button - BACK
     **/


    fun onBackButtonClick() {
        navigationManager.navigateBack()
    }


    /**
     * Button - Finish
     **/


    fun onFinishButtonClick() {
        val operation = if (deliverySharedViewModel.editMode) {
            DeliveryOperation.Update(
                id = deliverySharedViewModel.deliveryToEdit.id,
                data = prepareUpdateDeliveryRequest()
            )
        } else {
            DeliveryOperation.Create(
                data = prepareCreateDeliveryRequest()
            )
        }

        handleDeliveryOperation(operation)
    }


    private fun handleDeliveryOperation(operation: DeliveryOperation) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    when (operation) {
                        is DeliveryOperation.Create -> deliveryRepository.createDelivery(
                            prepareCreateDeliveryRequest()
                        )

                        is DeliveryOperation.Update -> deliveryRepository.updateDelivery(
                            operation.id,
                            operation.data
                        )
                    }
                },
                onSuccess = { onOperationSuccess() }
            )
        }
    }


    private fun onOperationSuccess() {
        navigationManager.navigateTo(NavigationItem.DeliveriesListAdmin)

        val messageId: Int = when (deliverySharedViewModel.editMode) {
            true -> R.string.delivery_update_success_message
            false -> R.string.delivery_create_success_message
        }

        showToast(
            context = context,
            text = context.getString(messageId),
            toastLength = Toast.LENGTH_LONG
        )
    }


    private fun prepareCreateDeliveryRequest(): DeliveryCreateRequest {
        return DeliveryCreateRequest(
            tasksIdList = selectedTasks.map { task -> DeliveryTask(task.id) },
            timeMinutes = _predictedTimeMinutes.intValue,
            distanceKm = distanceKm,
            employeeId = deliverySharedViewModel.employee.id,
            date = deliverySharedViewModel.selectedDate
        )
    }


    private fun prepareUpdateDeliveryRequest(): DeliveryUpdateRequest {
        return DeliveryUpdateRequest(
            tasksIdList = selectedTasks.map { task -> DeliveryTask(task.id) },
            timeMinutes = _predictedTimeMinutes.intValue,
            distanceKm = distanceKm,
            employeeId = deliverySharedViewModel.employee.id,
            date = deliverySharedViewModel.selectedDate
        )
    }


    sealed class DeliveryOperation {
        data class Create(val data: DeliveryCreateRequest) : DeliveryOperation()
        data class Update(val id: Long, val data: DeliveryUpdateRequest) :
            DeliveryOperation()
    }


    /**
     * Predicted Time
     **/


    fun getPredictedTimeText(): String {
        val hours = _predictedTimeMinutes.value / 60
        val minutes = _predictedTimeMinutes.intValue % 60

        return "${hours}h ${minutes}min"
    }


    fun getPredictedTimeTextColor(): Color {
        return if (_timeExceeded.value) DELIVERY_TIME_EXCEEDED_COLOR else DELIVERY_TIME_OK_COLOR
    }


    /**
     * Task Choose List
     **/


    private var _selectedTasks: MutableList<Task> = mutableStateListOf()
    val selectedTasks: List<Task>
        get() = _selectedTasks


    /**
     * Optimized route details
     */

    private val deliveryRepository = DeliveryRepository()

    private val _timeExceeded = mutableStateOf(false)
    private val _predictedTimeMinutes = mutableIntStateOf(0)
    private val _distanceKm = mutableIntStateOf(0)
    val distanceKm: Int
        get() = _distanceKm.intValue


    private fun fetchOptimizedRouteDetails() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    val optimizedRoutes =
                        deliveryRepository.getOptimizedRoutes(prepareOptimizeRoutesRequest())

                    if (optimizedRoutes.tasksOrder.size != selectedTasks.size)
                        throw OptimizationFailedException(context.getString(R.string.delivery_optimization_exception_message))

                    _distanceKm.intValue = optimizedRoutes.distanceKm
                    _timeExceeded.value = optimizedRoutes.timeExceeded
                    _predictedTimeMinutes.intValue = optimizedRoutes.timeMinutes

                    sortTasks(optimizedRoutes.tasksOrder)
                },
                onFailure = { navigationManager.navigateBack() }
            )
        }
    }


    private fun prepareOptimizeRoutesRequest(): OptimizeRoutesRequest {
        return OptimizeRoutesRequest(
            tasksToOptimize = selectedTasks.map { task ->
                TaskToOptimize(
                    id = task.id,
                    location = task.location
                )
            },
            date = deliverySharedViewModel.selectedDate
        )
    }


    private fun sortTasks(tasksOrder: LinkedList<TaskToOptimize>) {
        val sortedList = LinkedList<Task>()

        tasksOrder.forEach { optimizedTask ->
            val task = selectedTasks.find { it.id == optimizedTask.id }

            if (task == null)
                throw DataCorruptedException(context.getString(R.string.delivery_data_corrupted_exception_message))

            sortedList.add(task)
        }

        _selectedTasks.clear()
        _selectedTasks.addAll(sortedList)
    }


    /**
     * Initializer
     **/


    init {
        _selectedTasks.clear()
        _selectedTasks.addAll(deliverySharedViewModel.selectedTasks)

        fetchOptimizedRouteDetails()
    }

    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }

}