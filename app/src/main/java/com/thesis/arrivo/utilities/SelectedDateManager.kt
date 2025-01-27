package com.thesis.arrivo.utilities

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate

class SelectedDateManager {

    private var _selectedDate = mutableLongStateOf(getCurrentDateMillis())
    var selectedDate: Long
        get() = _selectedDate.longValue
        set(value) {
            _selectedDate.longValue = value
            _localDate.value = convertLongToLocalDate(value)
        }


    private var _localDate = mutableStateOf(convertLongToLocalDate(selectedDate))
    val localDate: LocalDate
        get() = _localDate.value
}