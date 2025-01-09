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
        title = "Task Details",
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            DialogRecord(
                label = "Title",
                value = task.title,
                maxLines = 2,
                textOverflow = TextOverflow.Ellipsis
            )

            DialogRecord(
                label = "Status",
                value = TasksListViewModel.getRenamedFilter(task.status)
            )

            DialogRecord(
                label = "Address",
                value = task.addressText,
                maxLines = 2,
                textOverflow = TextOverflow.Ellipsis
            )

            task.assignedDate?.let {
                DialogRecord(
                    label = "Date",
                    value = task.assignedDate
                )
            }

            task.employee?.let {
                DialogRecord(
                    label = "Employee",
                    value = "${task.employee.firstName} ${task.employee.lastName}"
                )
            }

            AlertDialogSingleButton(
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