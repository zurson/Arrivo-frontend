package com.thesis.arrivo.ui.admin.admin_accidents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.road_accidents.RoadAccident
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogSingleButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.view_models.RoadAccidentsViewModel

@Composable
fun RoadAccidentDetailsDialog(
    accident: RoadAccident,
    onDismiss: () -> Unit,
    onEditButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfoAlertDialog(
        title = stringResource(R.string.accidents_details_dialog_title),
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_title),
                value = "${accident.employee.firstName} ${accident.employee.lastName}",
                maxLines = 1,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_date),
                value = accident.date,
                maxLines = 1,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_status),
                value = accident.status,
                maxLines = 1,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_category),
                value = RoadAccidentsViewModel.formatAccidentCategory(accident.category),
                maxLines = 1,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_vehicle),
                value = accident.licensePlate,
                maxLines = 1,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.accidents_details_dialog_description),
                value = accident.description.replace("\n", " "),
                maxLines = 20,
                textOverflow = TextOverflow.Ellipsis
            )

            if (accident.status != RoadAccidentStatus.ENDED) {
                AlertDialogSingleButton(
                    text = stringResource(R.string.accidents_details_mark_as_resolved_button_text),
                    onEditButtonClick = { onEditButtonClick() },
                    icon = Icons.Filled.Edit,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.alert_dialog_button_top_padding))
                        .padding(horizontal = dimensionResource(R.dimen.alert_dialog_button_horizontal_padding))
                        .height(dimensionResource(R.dimen.alert_dialog_button_height))
                )
            }
        }
    }
}