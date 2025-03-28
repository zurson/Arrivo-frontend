package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.communication.delivery.RoutePoint
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.SelectedDateManager
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_ASSIGNED_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_COMPLETED_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_IN_PROGRESS_COLOR
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch

class DeliveriesListViewModel(
    private val context: Context,
    private val navigationManager: NavigationManager,
    private val loadingScreenManager: LoadingScreenManager,
    private val deliverySharedViewModel: DeliverySharedViewModel
) : ViewModel(), LoadingScreenStatusChecker {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    companion object {
        fun getRenamedFilter(filer: DeliveryStatus): String {
            return when (filer) {
                DeliveryStatus.COMPLETED -> "Completed"
                DeliveryStatus.IN_PROGRESS -> "In Delivery"
                DeliveryStatus.ASSIGNED -> "Assigned"
            }
        }


        fun getFilterColor(filer: DeliveryStatus): Color {
            return when (filer) {
                DeliveryStatus.COMPLETED -> DELIVERY_COMPLETED_COLOR
                DeliveryStatus.IN_PROGRESS -> DELIVERY_IN_PROGRESS_COLOR
                DeliveryStatus.ASSIGNED -> DELIVERY_ASSIGNED_COLOR
            }
        }

        private val selectedDateManager: SelectedDateManager = SelectedDateManager()

        private val _activeFilters = mutableStateListOf(DeliveryStatus.IN_PROGRESS)
        private val activeFilters: List<DeliveryStatus>
            get() = _activeFilters
    }


    /**
     * Date picker
     **/


    fun getSelectedDate(): Long = selectedDateManager.selectedDate


    fun onDateSelected(dateMillis: Long?) {
        selectedDateManager.selectedDate = dateMillis ?: getCurrentDateMillis()
        filterDeliveries()
    }


    /**
     * Filters
     **/

    fun getActiveFilters(): List<DeliveryStatus> = activeFilters

    fun toggleFilterActive(deliveryStatus: DeliveryStatus) {
        if (activeFilters.contains(deliveryStatus)) _activeFilters.remove(deliveryStatus)
        else _activeFilters.add(deliveryStatus)

        filterDeliveries()
    }


    /**
     * Create Delivery Button
     **/


    fun onCreateDeliveryButtonClick() {
        navigationManager.navigateTo(NavigationItem.DeliveryOptionsAdmin)
    }


    /**
     * Deliveries Filter
     **/

    private val _deliveriesToShow = mutableStateListOf<Delivery>()
    val deliveriesToShow: List<Delivery>
        get() = _deliveriesToShow


    private fun filterDeliveries() {
        viewModelScope.launch {
            val filtered = _deliveriesList.filter { item ->
                val matchesStatus = item.status in activeFilters || activeFilters.isEmpty()
                val matchesDate =
                    item.assignedDate.toEpochDay() == selectedDateManager.localDate.toEpochDay()
                matchesStatus && matchesDate
            }

            _deliveriesToShow.clear()
            _deliveriesToShow.addAll(filtered)
        }
    }


    /**
     * Deliveries List
     **/

    private val deliveryRepository = DeliveryRepository()

    private val _deliveriesList = mutableStateListOf<Delivery>()


    private fun fetchDeliveries() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _deliveriesList.clear()
                    _deliveriesList.addAll(deliveryRepository.getAllDeliveries())
                },
                onSuccess = { filterDeliveries() }
            )
        }
    }


    /**
     * Delivery Details
     **/

    private val _selectedDelivery = mutableStateOf(Delivery.emptyDelivery())
    val selectedDelivery: Delivery
        get() = _selectedDelivery.value

    private val _showDeliveryDetails = mutableStateOf(false)
    val showDeliveryDetails: Boolean
        get() = _showDeliveryDetails.value

    private val _selectedTask = mutableStateOf(Task.emptyTask())
    val selectedTask: Task
        get() = _selectedTask.value

    private val _showTaskDetails = mutableStateOf(false)
    val showTaskDetails: Boolean
        get() = _showTaskDetails.value


    private val _showDeliveryCancelConfirmationDialog = mutableStateOf(false)
    val showDeliveryCancelConfirmationDialog: Boolean
        get() = _showDeliveryCancelConfirmationDialog.value


    fun toggleShowDeliveryCancelConfirmationDialog() {
        _showDeliveryCancelConfirmationDialog.value = !_showDeliveryCancelConfirmationDialog.value
    }


    fun onDeliveryCancelConfirmationYesClick() {
        toggleShowDeliveryCancelConfirmationDialog()
        cancelDelivery()
    }


    private fun cancelDelivery() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = { deliveryRepository.cancelDelivery(selectedDelivery.id) },
                onSuccess = { onDeliveryCancelSuccess() }
            )
        }
    }


    private fun onDeliveryCancelSuccess() {
        navigationManager.navigateTo(NavigationItem.DeliveriesListAdmin, true)

        showToast(
            text = context.getString(R.string.delivery_cancel_success_message),
            toastLength = Toast.LENGTH_LONG
        )
    }


    fun onDeliveryCancelConfirmationNoClick() {
        toggleShowDeliveryCancelConfirmationDialog()
    }


    fun onDeliveryCancelConfirmationDismiss() {
        toggleShowDeliveryCancelConfirmationDialog()
    }


    fun toggleShowDeliveryDetails() {
        _showDeliveryDetails.value = !_showDeliveryDetails.value
    }


    fun toggleShowTaskDetails() {
        _showTaskDetails.value = !_showTaskDetails.value
    }


    fun onDeliverySelected(delivery: Delivery) {
        _selectedDelivery.value = delivery
        toggleShowDeliveryDetails()
    }


    fun onDeliveryDetailsDismiss() {
        toggleShowDeliveryDetails()
    }


    fun onDeliveryDetailsEditButtonClick() {
        toggleShowDeliveryDetails()

        deliverySharedViewModel.employee = selectedDelivery.employee

        deliverySharedViewModel.selectedTasks.clear()
        deliverySharedViewModel.selectedTasks.addAll(selectedDelivery.tasks)

        deliverySharedViewModel.selectedDate = selectedDelivery.assignedDate

        deliverySharedViewModel.deliveryToEdit = selectedDelivery

        navigationManager.navigateTo(NavigationItem.DeliveryOptionsEditAdmin)
    }


    fun onDeliveryDetailsCancelButtonClick() {
        toggleShowDeliveryDetails()
        toggleShowDeliveryCancelConfirmationDialog()
    }


    fun showDeliveryDetailsEditButton(delivery: Delivery): Boolean {
        return delivery.status != DeliveryStatus.COMPLETED
    }


    fun showDeliveryDetailsCancelButton(delivery: Delivery): Boolean {
        return delivery.status != DeliveryStatus.COMPLETED
    }


    fun onDeliveryDetailsTaskSelected(task: Task) {
        _selectedTask.value = task
        toggleShowTaskDetails()
    }


    fun onDeliveryDetailsTaskDialogDismiss() {
        toggleShowTaskDetails()
    }


    fun onDeliveryDetailsTaskDialogButtonClick() {
        toggleShowTaskDetails()
    }


    /**
     * Track
     **/


    private val _showTrackDialog = mutableStateOf(false)
    val showTrackDialog: Boolean
        get() = _showTrackDialog.value


    private val _waypoints = mutableStateListOf<RoutePoint>()
    val waypoints: List<RoutePoint>
        get() = _waypoints


    private fun toggleShowTrackDialog() {
        _showTrackDialog.value = !_showTrackDialog.value
    }


    fun onTrackButtonClick() {
        toggleShowDeliveryDetails()
        fetchTrack()
    }


    fun onTrackDialogDismiss() {
        toggleShowTrackDialog()
    }


    private fun fetchTrack() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _waypoints.clear()
                    val routePoints = deliveryRepository.getRoutePoints(selectedDelivery.id)
                    _waypoints.addAll(routePoints)
                },
                onSuccess = { onTrackFetchSuccess() }
            )
        }
    }


    private fun onTrackFetchSuccess() {
        toggleShowTrackDialog()
    }


    /**
     * Initializer
     **/


    init {
        fetchDeliveries()
    }

    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }
}