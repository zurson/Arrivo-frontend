package com.thesis.arrivo.ui.admin.admin_tasks

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
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.AppSpinner
import com.thesis.arrivo.components.AppTextField
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.view_models.NewTaskViewModel

@Composable
fun AddProductAlertDialog(
    modifier: Modifier = Modifier,
    newTaskViewModel: NewTaskViewModel
) {
    InfoAlertDialog(
        title = stringResource(R.string.add_product_dialog_title),
        onDismiss = { newTaskViewModel.toggleShowAddProductDialog() },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.alert_dialog_content_vertical_space)),
        ) {
            AppSpinner(
                items = newTaskViewModel.getAvailableProducts().map { productResponse -> productResponse.name },
                label = stringResource(R.string.add_product_spinner_label),
                onItemSelected = { newTaskViewModel.onProductSelected(it) },
                selectedItem = "",
                modifier = Modifier.fillMaxWidth(),
                isError = newTaskViewModel.isProductSpinnerError,
                errorMessage = stringResource(R.string.add_product_spinner_error_message)
            )

            AppTextField(
                value = newTaskViewModel.selectedProductAmount,
                onValueChange = { newTaskViewModel.onProductAmountValueChange(it) },
                label = stringResource(R.string.add_product_amount_label),
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth(),
                isError = newTaskViewModel.isProductAmountError,
                errorMessage = stringResource(R.string.add_product_amount_error_message)
            )

            AppButton(
                text = stringResource(R.string.add_product_button_text),
                icon = Icons.Outlined.AddCircle,
                onClick = { newTaskViewModel.onProductAddButtonClick() }
            )
        }
    }
}