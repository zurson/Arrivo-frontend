package com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.thesis.arrivo.R
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.AppTextField
import com.thesis.arrivo.components.GoogleMapView
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.TaskManagerViewModel


@Composable
fun LocationSearchAlertDialog(
    modifier: Modifier = Modifier,
    taskManagerViewModel: TaskManagerViewModel
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            taskManagerViewModel.selectedLocation,
            TaskManagerViewModel.DEFAULT_ZOOM
        )
    }

    InfoAlertDialog(
        title = stringResource(R.string.loc_search_dialog_title),
        onDismiss = { taskManagerViewModel.toggleLocationSearchDialog() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            SearchField(
                cameraPositionState = cameraPositionState,
                taskManagerViewModel = taskManagerViewModel
            )

            if (taskManagerViewModel.predictions.isEmpty()) {
                GoogleMapView(
                    selectedLocation = taskManagerViewModel.selectedLocation,
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.loc_search_dialog_map_height))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                )
            }

            AppButton(
                text = stringResource(R.string.loc_search_dialog_button_text),
                icon = Icons.Outlined.LocationOn,
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.loc_search_dialog_button_top_padding)),
                onClick = { taskManagerViewModel.onSelectLocationButtonClick() },
            )
        }
    }
}


@Composable
private fun SearchField(
    taskManagerViewModel: TaskManagerViewModel,
    cameraPositionState: CameraPositionState
) {
    AppTextField(
        value = taskManagerViewModel.query,
        onValueChange = { taskManagerViewModel.onSearchBarValueChange(it) },
        trailingIcon = Icons.Filled.Close,
        onTrailingIconClick = { taskManagerViewModel.onLocationSearchBarTrailingIconClick() },
        label = stringResource(R.string.loc_search_dialog_search_field_label),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        isError = taskManagerViewModel.locationSearchBarError,
        errorMessage = stringResource(R.string.loc_search_dialog_search_field_error_message)
    )

    LazyColumn {
        items(taskManagerViewModel.predictions) { prediction ->
            val locationText = taskManagerViewModel.getFullAddress(prediction)

            Text(
                text = locationText,
                fontSize = dpToSp(R.dimen.loc_search_dialog_predictions_text_size),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.loc_search_dialog_predictions_padding))
                    .clickable { taskManagerViewModel.onAddressClick(prediction, cameraPositionState) }
            )
        }
    }
}