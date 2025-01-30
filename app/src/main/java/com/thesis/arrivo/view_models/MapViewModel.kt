package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.libraries.navigation.Navigator.RouteStatus
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager

class MapViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModel() {

    private val _isNavigating = mutableStateOf(false)
    val isNavigating: Boolean
        get() = _isNavigating.value

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage.value

    private var _errorCode = mutableStateOf(RouteStatus.OK)

    private val _noDestination = mutableStateOf(false)
    val noDestination: Boolean
        get() = _noDestination.value


    fun startNavigation() {
        if (isNavigating)
            return

        val destination = mapSharedViewModel.destination
        _noDestination.value = destination == null

        if (noDestination)
            return

        NavigationApiManager.startNavigation(
            location = destination!!,
            navigationStartStatus = { status, code ->
                _errorCode.value = code
                _errorMessage.value = code.name
                _isNavigating.value = status
            }
        )
    }


    fun isError(): Boolean = _errorCode.value != RouteStatus.OK
}