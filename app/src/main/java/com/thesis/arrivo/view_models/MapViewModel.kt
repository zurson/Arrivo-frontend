package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.navigation.Navigator.RouteStatus
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.utilities.formatTime
import com.thesis.arrivo.utilities.getBreakTime
import com.thesis.arrivo.utilities.getDurationBetweenNowAndBreakTime
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.utilities.isDuringBreak
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager
import com.thesis.arrivo.utilities.showDefaultErrorDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class MapViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModel(), LoadingScreenStatusChecker {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

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

        if (noDestination || IS_NAVIGATING) return

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
     * Bottom Section
     **/


    private val _driveTime = mutableStateOf("")
    val driveTime: String
        get() = _driveTime.value

    private val _breakInTime = mutableStateOf("")
    val breakInTime: String
        get() = _breakInTime.value


    private fun startUpdatingTime() {
        viewModelScope.launch(Dispatchers.Main) {
            while (isActive) {
                _driveTime.value = getDriveTimeString()
                _breakInTime.value = getBreakInTimeString()

                _breakButtonEnabled.value = shouldBreakButtonBeEnabled()

                delay(1_000)
            }
        }
    }


    private fun shouldBreakButtonBeEnabled(): Boolean {
        val startTime = mapSharedViewModel.startTime

        if (startTime == null || mapSharedViewModel.breakTime != null) {
            return false
        }

        val breakStartTime = getBreakTime(startTime)
        val duration = getDurationBetweenNowAndBreakTime(breakStartTime)

        return duration.isNegative
    }


    private fun getDriveTimeString(): String {
        val startTime = mapSharedViewModel.startTime ?: return "---"
        val breakStart = mapSharedViewModel.breakTime

        var now = LocalDateTime.now()

        if (isDuringBreak(breakStart))
            now = breakStart

        val duration = Duration.between(startTime, now)

        val hours = duration.toHours()
        val minutes = duration.minusHours(hours).toMinutes()
        val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds

        return when {
            hours > 0 -> "${hours}h ${minutes}min"
            minutes > 0 -> "${minutes} min"
            seconds > 0 -> "${seconds}s"
            else -> "---"
        }
    }


    private fun getBreakInTimeString(): String {
        val startTime = mapSharedViewModel.startTime ?: return "---"

        val breakStartTime = getBreakTime(startTime)
        val duration = getDurationBetweenNowAndBreakTime(breakStartTime)

        if (duration.isNegative) {
            if (mapSharedViewModel.breakTime != null)
                return formatTime(mapSharedViewModel.breakTime!!)

            return "now"
        }

        val hours = duration.toHours()
        val minutes = duration.minusHours(hours).toMinutes()
        val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds

        return when {
            hours > 0 -> "${hours}h ${minutes}min"
            minutes > 0 -> "${minutes} min"
            seconds > 0 -> "${seconds}s"
            else -> "---"
        }
    }


    /**
     * Break Button
     **/


    private val deliveryRepository = DeliveryRepository()

    private val _breakButtonEnabled = mutableStateOf(false)
    val breakButtonEnabled: Boolean
        get() = _breakButtonEnabled.value


    fun onBreakButtonClick() {
        val deliveryId = mapSharedViewModel.deliveryId

        if (deliveryId == null) {
            showDefaultErrorDialog(
                context = context,
                title = context.getString(R.string.error_title),
                message = context.getString(R.string.unexpected_error),
            )
            return
        }

        notifyBreak(deliveryId)
    }


    private fun notifyBreak(deliveryId: Long) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    deliveryRepository.notifyBreakStart(deliveryId)
                },
                onSuccess = { onBreakNotifySuccess() },
                onFailure = { onBreakNotifyFailure() }
            )
        }
    }


    private fun onBreakNotifySuccess() {
        mapSharedViewModel.breakTime = LocalDateTime.now()
        _breakButtonEnabled.value = false
    }


    private fun onBreakNotifyFailure() {
        _breakButtonEnabled.value = true
    }


    /**
     * Initialize
     **/


    init {
        startUpdatingTime()
        startNavigation()
    }


    /**
     * Interface
     **/

    override fun isLoadingScreenEnabled(): Boolean {
        return loadingScreenManager.isLoadingScreenEnabled()
    }
}