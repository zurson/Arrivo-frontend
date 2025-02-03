package com.thesis.arrivo.ui.admin.admin_deliveries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.android.gms.maps.model.LatLng
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.delivery.RoutePoint
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.components.other_components.GoogleMapView
import com.thesis.arrivo.utilities.dpToSp


@Composable
fun DeliveryTrackDialog(
    waypoints: List<RoutePoint>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfoAlertDialog(
        title = stringResource(R.string.delivery_track_dialog_window_title),
        onDismiss = { onDismiss() },
        modifier = modifier.height(dimensionResource(R.dimen.track_dialog_height))
    ) {
        if (waypoints.isEmpty()) {
            NoContent()
            return@InfoAlertDialog
        }

        val start = waypoints.first().location
        val selectedLocation = LatLng(start.latitude, start.longitude)

        GoogleMapView(
            selectedLocation = selectedLocation,
            waypoints = waypoints,
        )
    }
}


@Composable
private fun NoContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.delivery_track_dialog_no_waypoints_message),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.track_dialog_no_waypoints_text_size),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}