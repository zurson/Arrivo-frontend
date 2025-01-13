package com.thesis.arrivo.components

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
import androidx.compose.ui.text.style.TextAlign
import com.thesis.arrivo.R
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun ConfirmationDialog(
    dialogTitle: String,
    lineOne: String?,
    lineTwo: String?,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InfoAlertDialog(
        title = dialogTitle,
        onDismiss = { onDismiss() },
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            lineOne?.let {
                Text(
                    text = lineOne,
                    fontSize = dpToSp(R.dimen.confirmation_dialog_text_size),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            lineTwo?.let {
                Text(
                    text = lineTwo,
                    fontWeight = FontWeight.Bold,
                    fontSize = dpToSp(R.dimen.confirmation_dialog_text_size),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.confirmation_dialog_buttons_horizontal_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(
                    text = stringResource(R.string.confirmation_dialog_yes_button_text),
                    onClick = { onYesClick() },
                    modifier = Modifier.weight(1f)
                )

                AppButton(
                    text = stringResource(R.string.confirmation_dialog_no_button_text),
                    onClick = { onNoClick() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}