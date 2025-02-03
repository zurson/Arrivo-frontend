package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.work_time.WorkTimeRepository
import com.thesis.arrivo.communication.work_time.WorkingHours
import com.thesis.arrivo.utilities.convertLongToLocalDate
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch

class WorkTimeViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
) : ViewModel(), LoadingScreenStatusChecker {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    /**
     * Selected Dates
     **/

    private val _selectedDates =
        mutableStateOf(Pair(getCurrentDateMillis(), getCurrentDateMillis()))
    val selectedDates: Pair<Long, Long>
        get() = _selectedDates.value


    fun onDateRangeSelected(dates: Pair<Long, Long>) {
        _selectedDates.value = dates
        fetchWorkTimeData()
    }


    /**
     * Work Time
     **/


    private val workTimeRepository = WorkTimeRepository()

    private val _workingHoursDataList = mutableStateListOf<WorkingHours>()
    val workingHoursDataList: List<WorkingHours>
        get() = _workingHoursDataList


    private fun fetchWorkTimeData() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _workingHoursDataList.clear()
                    _workingHoursDataList.addAll(
                        workTimeRepository.getWorkingTimeBetweenDates(
                            startDate = convertLongToLocalDate(selectedDates.first),
                            endDate = convertLongToLocalDate(selectedDates.second)
                        )
                    )
                },
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )
        }
    }


    private fun onSuccess() {
        toast(context.getString(R.string.work_time_fetch_success_toast_message))
    }


    private fun onFailure() {
        _workingHoursDataList.clear()
    }


    private fun toast(message: String) {
        showToast(
            text = message,
            toastLength = Toast.LENGTH_LONG
        )
    }


    /**
     * Initialize
     **/


    init {
        fetchWorkTimeData()
    }


    /**
     * Interfaces
     **/


    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }
}