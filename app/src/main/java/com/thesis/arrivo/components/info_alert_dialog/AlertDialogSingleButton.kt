package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import com.thesis.arrivo.R
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
            .padding(top = dimensionResource(R.dimen.alert_dialog_button_top_padding))
            .padding(horizontal = dimensionResource(R.dimen.alert_dialog_button_horizontal_padding))
    )
}