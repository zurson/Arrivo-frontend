package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.thesis.arrivo.R
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun <T> AppFilter(
    modifier: Modifier = Modifier,
    filter: T,
    isActive: Boolean,
    filterToString: (T) -> String,
    onSelected: (T) -> Unit
) {
    val color =
        if (isActive) Settings.FILTER_ACTIVE_COLOR else MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(color)
            .clickable { onSelected(filter) }
    ) {
        Text(
            text = filterToString(filter),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dpToSp(R.dimen.app_filter_text_size),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.app_filter_padding))
        )
    }
}