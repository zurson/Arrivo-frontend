package com.thesis.arrivo.utilities.location

import com.google.android.gms.maps.model.LatLng

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun toLatLon(): LatLng = LatLng(latitude, longitude)
}