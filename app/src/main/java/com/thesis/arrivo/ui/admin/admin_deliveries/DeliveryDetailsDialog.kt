package com.thesis.arrivo.ui.admin.admin_deliveries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogLazyColumn
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogLazyListDefaultContainer
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogSingleButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog

@Composable
fun DeliveryDetailsDialog(
    modifier: Modifier = Modifier,
    delivery: Delivery,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit,
    buttonIcon: ImageVector? = null,
    buttonText: String,
    onTaskSelected: (Task) -> Unit
) {
    InfoAlertDialog(
        title = stringResource(R.string.delivery_details_dialog_window_title),
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            DialogRecord(
                label = stringResource(R.string.delivery_details_dialog_record_status_label),
                value = delivery.status,
            )

            DialogRecord(
                label = stringResource(R.string.delivery_details_dialog_record_date_label),
                value = delivery.assignedDate.toString(),
            )

            DialogRecord(
                label = stringResource(R.string.delivery_details_dialog_record_distance_label),
                value = "${delivery.distanceKm} km",
            )

            DialogRecord(
                label = stringResource(R.string.delivery_details_dialog_record_time_label),
                value = formatPredictedTimeText(delivery.timeMinutes),
            )

            DialogRecord(
                label = stringResource(R.string.delivery_details_dialog_record_employee_label),
                value = "${delivery.employee.firstName} ${delivery.employee.lastName}",
                maxLines = 2,
            )

            AlertDialogLazyColumn(
                title = stringResource(R.string.delivery_details_dialog_tasks_list_title),
                listHeight = dimensionResource(R.dimen.dialog_lazy_column_height),
                items = delivery.tasks.map { task ->
                    {
                        TaskContainer(
                            task = task,
                            onTaskSelected = onTaskSelected
                        )
                    }
                },
            )

            AlertDialogSingleButton(
                text = buttonText,
                onEditButtonClick = { onButtonClick() },
                icon = buttonIcon,
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.alert_dialog_button_top_padding))
                    .padding(horizontal = dimensionResource(R.dimen.alert_dialog_button_horizontal_padding))
                    .height(dimensionResource(R.dimen.alert_dialog_button_height))
            )
        }
    }
}

private fun formatPredictedTimeText(timeInMinutes: Int): String {
    val hours = timeInMinutes / 60
    val minutes = timeInMinutes % 60

    return "${hours}h ${minutes}min"
}


@Composable
private fun TaskContainer(task: Task, onTaskSelected: (Task) -> Unit) {
    AlertDialogLazyListDefaultContainer(
        item = task,
        itemToLabelString = { it.title },
        itemToValueString = { it.addressText },
        onItemSelected = { onTaskSelected(it) },
        clickable = true
    )
}