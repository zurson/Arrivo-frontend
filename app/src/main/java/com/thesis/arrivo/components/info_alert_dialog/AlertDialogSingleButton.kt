package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.thesis.arrivo.R
import com.thesis.arrivo.components.AppButton

@Composable
fun AlertDialogSingleButton(
    onEditButtonClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    AppButton(
        onClick = { onEditButtonClick() },
        text = stringResource(R.string.employees_list_edit_button_text),
        icon = icon,
        modifier = modifier
    )
}