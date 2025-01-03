package com.thesis.arrivo.ui.admin.admin_employees

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thesis.arrivo.communication.employee.EmployeeResponse
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog

@Composable
fun EmployeesDetailsAlertDialog(
    modifier: Modifier = Modifier,
    emp: EmployeeResponse,
    onDismiss: () -> Unit
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

    }
}