package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.road_accidents.RoadAccident
import com.thesis.arrivo.communication.road_accidents.RoadAccidentsRepository
import com.thesis.arrivo.ui.admin.admin_accidents.RoadAccidentCategory
import com.thesis.arrivo.ui.admin.admin_accidents.RoadAccidentStatus
import com.thesis.arrivo.utilities.Settings.Companion.ROAD_ACCIDENTS_ACTIVE_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.ROAD_ACCIDENTS_FINISHED_COLOR
import com.thesis.arrivo.utilities.convertLongToLocalDate
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.mapError
import com.thesis.arrivo.utilities.showErrorDialog
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch
import java.time.LocalDate

class RoadAccidentsViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModel() {

    companion object {
        private var _selectedDate = mutableLongStateOf(getCurrentDateMillis())
        private val selectedDate: Long
            get() = _selectedDate.longValue

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


    private var selectedLocalDate: LocalDate = convertLongToLocalDate(selectedDate)


    fun getSelectedDate(): Long = selectedDate


    fun onDateSelected(dateMillis: Long?) {
        _selectedDate.longValue = dateMillis ?: getCurrentDateMillis()
        selectedLocalDate = convertLongToLocalDate(selectedDate)
        filterList()
    }


    /**
     * Filters
     **/

    fun getActiveFilters() = activeFilters

    fun toggleFilterActive(filter: RoadAccidentStatus) {
        if (activeFilters.contains(filter)) _activeFilters.remove(filter)
        else _activeFilters.add(filter)

        filterList()
    }


    private fun filterList() {
        _accidentsToShow.clear()

        val filteredAccidents = _allAccidents.filter { accident ->
            val isDateMatching = accident.date.toEpochDay() == selectedLocalDate.toEpochDay()
            val isStatusMatching =
                activeFilters.isEmpty() || activeFilters.contains(accident.status)
            isDateMatching && isStatusMatching
        }

        _accidentsToShow.addAll(filteredAccidents)
    }


    /**
     * Fetching and Sending Data
     **/

    private val roadAccidentsRepository = RoadAccidentsRepository()

    private val _accidentsToShow = mutableStateListOf<RoadAccident>()
    val accidentsToShow: List<RoadAccident>
        get() = _accidentsToShow

    private val _allAccidents = mutableStateListOf<RoadAccident>()

    private fun fetchRoadAccidents() {
        viewModelScope.launch {
            try {
                loadingScreenManager.showLoadingScreen()
                _allAccidents.clear()
                _allAccidents.addAll(roadAccidentsRepository.getAllRoadAccidents())
                filterList()
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                loadingScreenManager.hideLoadingScreen()
            }
        }
    }


    private fun onFailure(exception: java.lang.Exception) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = mapError(exception, context)
        )
    }


    init {
        fetchRoadAccidents()
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

    var showAccidentDetailsDialog by mutableStateOf(false)
    private val _selectedAccident = mutableStateOf(RoadAccident.emptyRoadAccident())
    val selectedAccident: RoadAccident
        get() = _selectedAccident.value


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


    fun onAccidentFinishButtonClick() {
        toggleShowAccidentDetailsDialog()
        markAccidentAsResolved()
    }


    private fun markAccidentAsResolved() {
        viewModelScope.launch {
            try {
                loadingScreenManager.showLoadingScreen()
                roadAccidentsRepository.markRoadAccidentAsResolved(selectedAccident.id)
                onUpdateSuccess()
            } catch (e: Exception) {
                loadingScreenManager.hideLoadingScreen()
                onFailure(e)
            }
        }
    }


    private fun onUpdateSuccess() {
        showToast(
            context = context,
            text = context.getString(R.string.accidents_mark_as_resolved_success_text),
            toastLength = Toast.LENGTH_LONG
        )

        fetchRoadAccidents()
    }

}