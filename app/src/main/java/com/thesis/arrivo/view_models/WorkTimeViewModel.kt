package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.utilities.getCurrentDateMillis
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager

class WorkTimeViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
) : ViewModel() {

    /**
     * Selected Dates
     **/

    private val _selectedDates =
        mutableStateOf(Pair(getCurrentDateMillis(), getCurrentDateMillis()))
    val selectedDates: Pair<Long, Long>
        get() = _selectedDates.value


    fun onDateRangeSelected(dates: Pair<Long, Long>) {
        _selectedDates.value = dates
    }

}