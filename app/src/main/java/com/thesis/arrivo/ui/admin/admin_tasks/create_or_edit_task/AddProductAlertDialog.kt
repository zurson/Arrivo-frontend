package com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppSpinner
import com.thesis.arrivo.components.other_components.AppTextField
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.view_models.TaskManagerViewModel

@Composable
fun AddProductAlertDialog(
    modifier: Modifier = Modifier,
    taskManagerViewModel: TaskManagerViewModel
) {
    InfoAlertDialog(
        title = stringResource(R.string.add_product_dialog_title),
        onDismiss = { taskManagerViewModel.toggleShowAddProductDialog() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            AppSpinner(
                items = taskManagerViewModel.getAvailableProducts().map { productResponse -> productResponse.name },
                label = stringResource(R.string.add_product_spinner_label),
                onItemSelected = { taskManagerViewModel.onProductSelected(it) },
                selectedItem = "",
                modifier = Modifier.fillMaxWidth(),
                isError = taskManagerViewModel.isProductSpinnerError,
                errorMessage = stringResource(R.string.add_product_spinner_error_message)
            )

            AppTextField(
                value = taskManagerViewModel.selectedProductAmount,
                onValueChange = { taskManagerViewModel.onProductAmountValueChange(it) },
                label = stringResource(R.string.add_product_amount_label),
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth(),
                isError = taskManagerViewModel.isProductAmountError,
                errorMessage = stringResource(R.string.add_product_amount_error_message)
            )

            AppButton(
                text = stringResource(R.string.add_product_button_text),
                icon = Icons.Outlined.AddCircle,
                onClick = { taskManagerViewModel.onProductAddButtonClick() }
            )
        }
    }
}