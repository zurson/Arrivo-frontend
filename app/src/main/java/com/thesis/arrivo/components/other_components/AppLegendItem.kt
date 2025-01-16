package com.thesis.arrivo.components.other_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AppLegendItem(
    filterName: String,
    circleColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.legend_item_horizontal_space)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Circle(
            size = dimensionResource(R.dimen.legend_item_circle_size),
            color = circleColor
        )

        Text(
            text = filterName,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.legend_item_text_size)
        )
    }
}