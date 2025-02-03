package com.thesis.arrivo.components.other_components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.delivery.RoutePoint
import com.thesis.arrivo.utilities.Settings.Companion.DEFAULT_MAP_ZOOM
import com.thesis.arrivo.utilities.Settings.Companion.POLYLINE_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.POLYLINE_WIDTH

@SuppressLint("UnrememberedMutableState")
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    selectedLocation: LatLng,
    waypoints: List<RoutePoint> = emptyList(),
    cameraPositionState: CameraPositionState? = null,
) {
    val usingPositionState = cameraPositionState ?: createCameraPositionState(selectedLocation)

    LaunchedEffect(selectedLocation.latitude, selectedLocation.longitude) {
        usingPositionState.move(
            CameraUpdateFactory.newLatLngZoom(selectedLocation, DEFAULT_MAP_ZOOM)
        )
    }

    val drawPolyline = waypoints.isNotEmpty()
    val startMarkerTitle =
        if (drawPolyline) stringResource(R.string.delivery_track_start_point_marker_title) else null
    val endMarkerTitle =
        if (drawPolyline) stringResource(R.string.delivery_track_end_point_marker_title) else null

    Box(
        modifier = modifier
    ) {
        GoogleMap(
            cameraPositionState = usingPositionState,
        ) {
            Marker(
                state = MarkerState(position = selectedLocation),
                title = startMarkerTitle,
            )

            if (drawPolyline) {
                val endLocation = waypoints.last().location

                Marker(
                    state = MarkerState(
                        position = LatLng(
                            endLocation.latitude,
                            endLocation.longitude
                        )
                    ),
                    title = endMarkerTitle
                )

                Polyline(
                    points = waypoints.map { LatLng(it.location.latitude, it.location.longitude) },
                    color = POLYLINE_COLOR,
                    width = POLYLINE_WIDTH
                )
            }
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
