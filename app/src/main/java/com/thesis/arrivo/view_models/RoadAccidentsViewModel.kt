package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.road_accidents.RoadAccident
import com.thesis.arrivo.communication.road_accidents.RoadAccidentsRepository
import com.thesis.arrivo.ui.common.road_accidents_list.RoadAccidentCategory
import com.thesis.arrivo.ui.common.road_accidents_list.RoadAccidentStatus
import com.thesis.arrivo.utilities.SelectedDateManager
import com.thesis.arrivo.utilities.Settings.Companion.ROAD_ACCIDENTS_ACTIVE_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.ROAD_ACCIDENTS_FINISHED_COLOR
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.preparePhoneCall
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch

abstract class RoadAccidentsViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModel(), LoadingScreenStatusChecker {

    protected val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    companion object {
        private val selectedDateManager: SelectedDateManager = SelectedDateManager()

        private val _activeFilters = mutableStateListOf(RoadAccidentStatus.ACTIVE)
        private val activeFilters: List<RoadAccidentStatus>
            get() = _activeFilters

        fun formatAccidentCategory(roadAccidentCategory: RoadAccidentCategory): String {
            return roadAccidentCategory.name.replace("_", " ")
        }
    }

    /**
     * Date picker
     **/


    fun getSelectedDate(): Long = selectedDateManager.selectedDate


    fun onDateSelected(dateMillis: Long?) {
        selectedDateManager.selectedDate = dateMillis ?: getCurrentDateMillis()
        filterList()
    }


    /**
     * Filters
     **/


    fun toggleFilterActive(filter: RoadAccidentStatus) {
        if (activeFilters.contains(filter)) _activeFilters.remove(filter)
        else _activeFilters.add(filter)

        filterList()
    }


    private fun filterList() {
        _accidentsToShow.clear()

        val filteredAccidents = _allAccidents.filter { accident ->
            val isStatusMatching =
                activeFilters.isEmpty() || activeFilters.contains(accident.status)
            val isDateMatching = if (accident.status == RoadAccidentStatus.ACTIVE) {
                true
            } else {
                accident.date.toEpochDay() == selectedDateManager.localDate.toEpochDay()
            }
            isDateMatching && isStatusMatching
        }

        _accidentsToShow.addAll(filteredAccidents)
    }


    fun isFilterActive(filter: RoadAccidentStatus): Boolean {
        return activeFilters.contains(filter)
    }


    fun filterToString(filter: RoadAccidentStatus): String {
        return capitalize(filter.name.lowercase())
    }


    /**
     * Fetching and Sending Data
     **/

    protected val roadAccidentsRepository = RoadAccidentsRepository()

    private val _accidentsToShow = mutableStateListOf<RoadAccident>()
    val accidentsToShow: List<RoadAccident>
        get() = _accidentsToShow

    protected val _allAccidents = mutableStateListOf<RoadAccident>()


    abstract fun fetchRoadAccidents()


    protected fun onFetchAccidentsSuccess() {
        filterList()
    }


    /**
     * Road Accident Container
     **/


    fun getIndicatorColor(roadAccidentStatus: RoadAccidentStatus): Color {
        return when (roadAccidentStatus) {
            RoadAccidentStatus.ACTIVE -> ROAD_ACCIDENTS_ACTIVE_COLOR
            RoadAccidentStatus.ENDED -> ROAD_ACCIDENTS_FINISHED_COLOR
        }
    }


    /**
     * Road Accident Details
     **/

    protected var showMarkAsResolvedButton by mutableStateOf(true)

    var showAccidentDetailsDialog by mutableStateOf(false)
    private val _selectedAccident = mutableStateOf(RoadAccident.emptyRoadAccident())
    val selectedAccident: RoadAccident
        get() = _selectedAccident.value

    var showConfirmationDialog by mutableStateOf(false)
    var showAccidentLocationOnMap by mutableStateOf(false)


    private fun toggleShowAccidentLocationOnMap() {
        showAccidentLocationOnMap = !showAccidentLocationOnMap
    }


    private fun toggleShowConfirmationDialog() {
        showConfirmationDialog = !showConfirmationDialog
    }


    private fun toggleShowAccidentDetailsDialog() {
        showAccidentDetailsDialog = !showAccidentDetailsDialog
    }


    fun onRoadAccidentSelected(roadAccident: RoadAccident) {
        _selectedAccident.value = roadAccident
        toggleShowAccidentDetailsDialog()
    }


    fun onAccidentDialogDismiss() {
        toggleShowAccidentDetailsDialog()
    }


    fun onAccidentMarkAsResolvedButtonClick() {
        toggleShowAccidentDetailsDialog()
        toggleShowConfirmationDialog()
    }


    fun onCallButtonClick() {
        toggleShowAccidentDetailsDialog()

        val phoneNumber = selectedAccident.employee.phoneNumber
        preparePhoneCall(context, phoneNumber)
    }


    fun onMapButtonClick() {
        toggleShowAccidentDetailsDialog()

        toggleShowAccidentLocationOnMap()
    }


    fun onAccidentLocationMapDismiss() {
        toggleShowAccidentLocationOnMap()
    }


    private fun markAccidentAsResolved() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    roadAccidentsRepository.markRoadAccidentAsResolved(selectedAccident.id)
                },
                onSuccess = { onUpdateSuccess() }
            )
        }
    }


    private fun onUpdateSuccess() {
        showToast(
            text = context.getString(R.string.accidents_mark_as_resolved_success_text),
            toastLength = Toast.LENGTH_LONG
        )

        fetchRoadAccidents()
    }


    fun onConfirmationYesClick() {
        toggleShowConfirmationDialog()
        toggleShowAccidentDetailsDialog()
        markAccidentAsResolved()
    }


    fun onConfirmationNoClick() {
        toggleShowConfirmationDialog()
    }


    fun onConfirmationDismiss() {
        toggleShowConfirmationDialog()
    }


    fun showMainButtonDialog(): Boolean = showMarkAsResolvedButton


    /**
     * Interface
     **/


    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }

}