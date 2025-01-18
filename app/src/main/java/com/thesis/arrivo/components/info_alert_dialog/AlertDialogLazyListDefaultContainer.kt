package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.ArrowRightIcon
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun <T> AlertDialogLazyListDefaultContainer(
    item: T,
    itemToLabelString: (T) -> String,
    itemToValueString: (T) -> String,
    onItemSelected: (T) -> Unit = { },
    clickable: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier
            .clickable(
                enabled = clickable,
                onClick = { onItemSelected(item) }
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(dimensionResource(R.dimen.dialog_lazy_column_default_container_padding))
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dialog_lazy_column_default_container_vertical_space)),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = itemToLabelString(item),
                fontSize = dpToSp(R.dimen.dialog_record_label_text_size),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = itemToValueString(item),
                fontSize = dpToSp(R.dimen.dialog_record_value_text_size),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (clickable)
            ArrowRightIcon(size = dimensionResource(R.dimen.dialog_lazy_column_icon_size))
    }
}