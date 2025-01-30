package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.utilities.location.Location

class MapSharedViewModel: ViewModel() {

    private val _destination = mutableStateOf<Location?>(null)
    var destination: Location?
        get() = _destination.value
        set(value) { _destination.value = value }

}