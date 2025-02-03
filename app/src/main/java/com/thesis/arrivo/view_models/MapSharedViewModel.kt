package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.utilities.location.Location
import java.time.LocalDateTime

class MapSharedViewModel : ViewModel() {

    private val _destination = mutableStateOf<Location?>(null)
    var destination: Location?
        get() = _destination.value
        set(value) {
            _destination.value = value
        }

    private val _startTime = mutableStateOf<LocalDateTime?>(null)
    var startTime: LocalDateTime?
        get() = _startTime.value
        set(value) {
            _startTime.value = value
        }

    private val _breakTime = mutableStateOf<LocalDateTime?>(null)
    var breakTime: LocalDateTime?
        get() = _breakTime.value
        set(value) {
            _breakTime.value = value
        }

    var deliveryId: Long? = null
}