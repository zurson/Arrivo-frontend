package com.thesis.arrivo.ui.admin.admin_tasks.create_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.thesis.arrivo.R
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.NewTaskViewModel

@Composable
fun ProductDeleteConfirmationDialog(
    modifier: Modifier = Modifier,
    newTaskViewModel: NewTaskViewModel,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit
) {
    InfoAlertDialog(
        title = stringResource(R.string.product_delete_confirmation_dialog_title),
        onDismiss = { newTaskViewModel.toggleShowProductDeleteConfirmationDialog() },
        modifier = modifier,
    ) {
        val productToDelete = newTaskViewModel.productToDelete

        if (productToDelete == null) {
            newTaskViewModel.toggleShowProductDeleteConfirmationDialog()
            return@InfoAlertDialog
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            Text(
                text = productToDelete.name,
                fontSize = dpToSp(R.dimen.product_delete_confirmation_product_details_text_size),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "${productToDelete.amount} pcs",
                fontWeight = FontWeight.Bold,
                fontSize = dpToSp(R.dimen.product_delete_confirmation_product_details_text_size),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.product_delete_confirmation_product_buttons_horizontal_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(
                    text = stringResource(R.string.product_delete_confirmation_dialog_yes_button_text),
                    onClick = { onYesClick() },
                    modifier = Modifier.weight(1f)
                )

                AppButton(
                    text = stringResource(R.string.product_delete_confirmation_dialog_no_button_text),
                    onClick = { onNoClick() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}