package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.libraries.navigation.Navigator.RouteStatus
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager

class MapViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModel(), LoadingScreenStatusChecker {

    companion object {
        private var IS_NAVIGATING = false
        private var LAST_DESTINATION: Location? = null
    }

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage.value

    private var _errorCode = mutableStateOf(RouteStatus.OK)

    private val _noDestination = mutableStateOf(false)
    val noDestination: Boolean
        get() = _noDestination.value


    /**
     * Navigation
     **/


    private fun startNavigation() {
        val destination = mapSharedViewModel.destination

        if (LAST_DESTINATION != destination) {
            NavigationApiManager.stopNavigation()
            IS_NAVIGATING = false
        }

        LAST_DESTINATION = destination
        _noDestination.value = destination == null
        if (noDestination) return

        if (IS_NAVIGATING) return

        NavigationApiManager.startNavigation(
            location = destination!!,
            navigationStartStatus = { status, code ->
                _errorCode.value = code
                _errorMessage.value = code.name
                IS_NAVIGATING = status
            }
        )
    }


    fun isError(): Boolean = _errorCode.value != RouteStatus.OK


    /**
     * Initialize
     **/


    init {
        startNavigation()
    }


    /**
     * Interface
     **/

    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }
}