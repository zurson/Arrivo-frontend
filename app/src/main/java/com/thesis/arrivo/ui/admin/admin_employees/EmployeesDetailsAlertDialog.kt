package com.thesis.arrivo.ui.admin.admin_employees

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
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.components.info_alert_dialog.AlertDialogSingleButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.utilities.formatPhoneNumber

@Composable
fun EmployeesDetailsAlertDialog(
    modifier: Modifier = Modifier,
    emp: Employee,
    onDismiss: () -> Unit,
    onEditButtonClick: () -> Unit
) {
    InfoAlertDialog(
        title = "${emp.firstName} ${emp.lastName}",
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            DialogRecord(
                label = stringResource(R.string.employee_details_dialog_record_email_label),
                value = emp.email
            )

            DialogRecord(
                label = stringResource(R.string.employee_details_dialog_record_phone_label),
                value = emp.phoneNumber,
                valueFormatter = { value -> formatPhoneNumber("$value") }
            )

            DialogRecord(
                label = stringResource(R.string.employee_details_dialog_record_status_label),
                value = emp.status
            )

            AlertDialogSingleButton(
                text = stringResource(R.string.employees_list_edit_button_text),
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