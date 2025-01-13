package com.thesis.arrivo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.thesis.arrivo.utilities.Settings.Companion.DEFAULT_MAP_ZOOM

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    selectedLocation: LatLng,
    cameraPositionState: CameraPositionState? = null
) {
    val usingPositionState = cameraPositionState ?: createCameraPositionState(selectedLocation)

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            cameraPositionState = usingPositionState
        ) {
            Marker(state = MarkerState(position = selectedLocation))
        }
    }
}


@Composable
private fun createCameraPositionState(selectedLocation: LatLng): CameraPositionState {
    return rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            selectedLocation,
            DEFAULT_MAP_ZOOM
        )
    }
}