package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.delivery.DeliveryRepository
import com.thesis.arrivo.communication.delivery.RoutePoint
import com.thesis.arrivo.communication.delivery.TrackPointInsertRequest
import com.thesis.arrivo.utilities.Settings.Companion.TRACKER_MAX_LIST_CAPACITY
import com.thesis.arrivo.utilities.Settings.Companion.TRACK_SENDING_INTERVAL_MS
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.navigation_api.LocationTracker
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantLock

class MapSharedViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModel() {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)


    /**
     *  Main
     **/

    private val _destination = mutableStateOf<Location?>(null)
    val destination: Location?
        get() = _destination.value

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


    fun setDestination(location: Location?) {
        _destination.value = location

        if (location == null)
            stopTracking()
        else
            startTracking()
    }


    /**
     * Location Tracking
     **/


    private val deliveryRepository = DeliveryRepository()
    private val locationTracker = LocationTracker(context = MainActivity.context)
    private var routesSenderJob: Job? = null
    private val routePoints: MutableList<RoutePoint> = mutableListOf()
    private val lock = ReentrantLock()


    fun startTracking() {
        if (routesSenderJob == null || !routesSenderJob!!.isActive)
            startRoutePointsSender()

        locationTracker.startTracking { location ->
            lock.lock()
            val loc = Location(location.latitude, location.longitude)
            if (canAddPointToList()) routePoints.add(RoutePoint(loc, LocalDateTime.now()))
            lock.unlock()

            println(location)
        }
    }


    private fun canAddPointToList(): Boolean {
        return routePoints.size <= TRACKER_MAX_LIST_CAPACITY
    }


    private fun stopTracking() {
        locationTracker.stopTracking()
        routesSenderJob?.cancel()
        routesSenderJob = null
    }


    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }


    private fun startRoutePointsSender() {
        routesSenderJob = viewModelScope.launch {
            while (isActive) {
                val id = deliveryId

                if (id == null) {
                    routesSenderJob = null
                    cancel()
                    return@launch
                }

                lock.lock()
                if (routePoints.size > 0) sendRoutePointsToServer(id)
                lock.unlock()
                delay(TRACK_SENDING_INTERVAL_MS)
            }
        }
    }


    private suspend fun sendRoutePointsToServer(deliveryId: Long) {
        serverRequestManager.sendRequest(
            actionToPerform = {
                deliveryRepository.addRoutePoints(TrackPointInsertRequest(routePoints, deliveryId))
            },
            onSuccess = { onSuccess() },
            useLoadingScreen = false
        )
    }


    private fun onSuccess() {
        routePoints.clear()
    }

}