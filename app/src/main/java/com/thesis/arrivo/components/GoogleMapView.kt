package com.thesis.arrivo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    selectedLocation: LatLng?,
    cameraPositionState: CameraPositionState
) {
    Box(
        modifier = modifier
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState
        ) {
            selectedLocation?.let { Marker(state = MarkerState(position = it)) }
        }
    }
}