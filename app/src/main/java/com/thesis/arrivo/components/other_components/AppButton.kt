package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.thesis.arrivo.R
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.Settings.Companion.APP_BUTTON_DEFAULT_MAX_LINES
import com.thesis.arrivo.utilities.Settings.Companion.APP_BUTTON_DISABLE_ALPHA
import com.thesis.arrivo.utilities.dpToSp
import kotlin.math.max

@Composable
fun AppButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector? = null,
    maxLines: Int = APP_BUTTON_DEFAULT_MAX_LINES,
    iconStart: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val buttonColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onPrimary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .bounceClick(enabled)
            .clickable(enabled = enabled) { if (enabled) onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(if (enabled) buttonColor else buttonColor.copy(alpha = APP_BUTTON_DISABLE_ALPHA))
            .padding(horizontal = dimensionResource(R.dimen.app_button_padding))
    ) {
        if (iconStart) {
            icon?.let { DefaultAppButtonIcon(icon, enabled) }
        }

        Text(
            text = text,
            maxLines = max(maxLines, APP_BUTTON_DEFAULT_MAX_LINES),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontSize = dpToSp(R.dimen.app_button_text_size),
            color = if (enabled) textColor else textColor.copy(alpha = APP_BUTTON_DISABLE_ALPHA),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = dimensionResource(R.dimen.app_button_text_padding))
        )

        if (!iconStart) {
            icon?.let { DefaultAppButtonIcon(icon, enabled) }
        }
    }
}


@Composable
private fun DefaultAppButtonIcon(icon: ImageVector, enabled: Boolean) {
    val iconColor = MaterialTheme.colorScheme.onPrimary

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (enabled) iconColor else iconColor.copy(alpha = APP_BUTTON_DISABLE_ALPHA),
        modifier = Modifier
            .requiredSize(dimensionResource(R.dimen.app_button_icon_size))
    )
}


@Preview
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
        AppButton(
            onClick = {},
            text = "Create account",
            icon = Icons.Outlined.Close,
            enabled = false
        )
    }
}