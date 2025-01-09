package com.thesis.arrivo.utilities

import com.google.android.gms.maps.model.LatLng

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    fun toLatLon(): LatLng = LatLng(latitude, longitude)
}