package com.thesis.arrivo.ui.admin.admin_tasks.tasks_list

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
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogSingleButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.view_models.TasksListViewModel

@Composable
fun TaskDetailsDialog(
    modifier: Modifier = Modifier,
    task: Task,
    onDismiss: () -> Unit,
    onEditButtonClick: () -> Unit
) {
    InfoAlertDialog(
        title = stringResource(R.string.tasks_list_details_dialog_window_title),
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            DialogRecord(
                label = stringResource(R.string.tasks_list_details_dialog_title),
                value = task.title,
                maxLines = 2,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = stringResource(R.string.tasks_list_details_dialog_status),
                value = TasksListViewModel.getRenamedFilter(task.status)
            )

            DialogRecord(
                label = stringResource(R.string.tasks_list_details_dialog_address),
                value = task.addressText,
                maxLines = 2,
                textOverflow = TextOverflow.Ellipsis
            )

            task.assignedDate?.let {
                DialogRecord(
                    label = stringResource(R.string.tasks_list_details_dialog_date),
                    value = task.assignedDate.toLocalDate()
                )
            }

            task.employee?.let {
                DialogRecord(
                    label = stringResource(R.string.tasks_list_details_dialog_employee),
                    value = "${task.employee.firstName} ${task.employee.lastName}"
                )
            }

            AlertDialogSingleButton(
                text = stringResource(R.string.tasks_list_details_dialog_button_text),
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