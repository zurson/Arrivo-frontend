package com.thesis.arrivo.ui.admin.admin_employees

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog

@Composable
fun EmployeesDetailsAlertDialog(
    modifier: Modifier = Modifier,
    emp: EmployeeResponse,
    onDismiss: () -> Unit,
    onEditButtonClick: () -> Unit
) {
    InfoAlertDialog(
        title = "${emp.firstName} ${emp.lastName}",
        onDismiss = { onDismiss() },
        modifier = modifier
    ) {

        DialogRecord(
            label = "Email",
            value = emp.email
        )

        DialogRecord(
            label = "Phone",
            value = emp.phoneNumber,
            valueFormatter = { value ->
                "${value.toString().substring(0, 3)}-${
                    value.toString().substring(3, 6)
                }-${value.toString().substring(6)}"
            }
        )

        DialogRecord(
            label = "Status",
            value = emp.status
        )

        AppButton(
            onClick = { onEditButtonClick() },
            text = stringResource(R.string.employees_list_edit_button_text),
            icon = Icons.Outlined.Edit,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.employee_details_edit_button_top_padding))
                .padding(horizontal = dimensionResource(R.dimen.employee_details_edit_button_horizontal_padding))
                .height(dimensionResource(R.dimen.employee_details_edit_button_height))
        )
    }
}