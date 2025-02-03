package com.thesis.arrivo.utilities.navigation_api

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.thesis.arrivo.utilities.Settings.Companion.TRACKER_FASTEST_UPDATE_INTERVAL
import com.thesis.arrivo.utilities.Settings.Companion.TRACKER_MIN_BEARING_CHANGE
import com.thesis.arrivo.utilities.Settings.Companion.TRACKER_MIN_DISTANCE_METERS
import com.thesis.arrivo.utilities.Settings.Companion.TRACKER_UPDATE_INTERVAL
import kotlin.math.abs

class LocationTracker(private val context: Context) {
    private val minDistanceMeters: Float = TRACKER_MIN_DISTANCE_METERS
    private val minBearingChange: Float = TRACKER_MIN_BEARING_CHANGE
    private val updateInterval: Long = TRACKER_UPDATE_INTERVAL
    private val fastestInterval: Long = TRACKER_FASTEST_UPDATE_INTERVAL

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private var lastLocation: Location? = null
    private var lastBearing: Float? = null

    private var locationCallback: LocationCallback? = null

    fun startTracking(onLocationUpdate: (LatLng) -> Unit) {
        if (locationCallback != null)
            return

        val request = createLocationRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                processNewLocations(result.locations, onLocationUpdate)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())
    }

    fun stopTracking() {
        locationCallback?.let { locationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateInterval).apply {
            setMinUpdateIntervalMillis(fastestInterval)
        }.build()
    }

    private fun processNewLocations(locations: List<Location>, onLocationUpdate: (LatLng) -> Unit) {
        locations.forEach { location ->
            if (isNewPointValid(location)) {
                onLocationUpdate(LatLng(location.latitude, location.longitude))
                lastLocation = location
                lastBearing = location.bearing
            }
        }
    }

    private fun isNewPointValid(location: Location): Boolean {
        val isFarEnough =
            lastLocation == null || lastLocation!!.distanceTo(location) > minDistanceMeters
        val isTurn = lastBearing == null || abs(location.bearing - lastBearing!!) > minBearingChange
        return isFarEnough || isTurn
    }
}

