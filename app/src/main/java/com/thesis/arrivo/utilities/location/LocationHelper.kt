package com.thesis.arrivo.utilities.location

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class LocationHelper(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(
        onSuccess: (Location) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onSuccess(Location(location.latitude, location.longitude))
                    } else {
                        onFailure(Exception("Location is null"))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: SecurityException) {
            onFailure(e)
        }
    }
}
