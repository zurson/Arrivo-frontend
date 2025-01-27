package com.thesis.arrivo.ui.admin.admin_tasks.tasks_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogLazyColumn
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogLazyListDefaultContainer
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogSingleButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.communication.task.Product
import com.thesis.arrivo.view_models.TasksListViewModel

@Composable
fun TaskDetailsDialog(
    modifier: Modifier = Modifier,
    task: Task,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit,
    buttonIcon: ImageVector? = null,
    buttonText: String
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
                    value = task.assignedDate
                )
            }

            task.employee?.let {
                DialogRecord(
                    label = stringResource(R.string.tasks_list_details_dialog_employee),
                    value = "${task.employee.firstName} ${task.employee.lastName}"
                )
            }

            AlertDialogLazyColumn(
                title = "Products",
                items = task.products.map { product -> { ProductContainer(product) } },
                listHeight = dimensionResource(R.dimen.dialog_lazy_column_height),
            )

            AlertDialogSingleButton(
                text = buttonText,
                onEditButtonClick = { onButtonClick() },
                icon = buttonIcon,
            )
        }
    }
}


@Composable
private fun ProductContainer(product: Product) {
    AlertDialogLazyListDefaultContainer(
        item = product,
        itemToLabelString = { product.name },
        itemToValueString = { "${product.amount} psc" },
        clickable = false
    )
}
