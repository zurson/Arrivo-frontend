package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.thesis.arrivo.components.other_components.AppButton

@Composable
fun AlertDialogSingleButton(
    text: String,
    onEditButtonClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    AppButton(
        onClick = { onEditButtonClick() },
        text = text,
        icon = icon,
        modifier = modifier
    )
}