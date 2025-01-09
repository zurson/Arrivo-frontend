package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun DialogRecord(
    label: String,
    value: Any,
    maxLines: Int = 1,
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    valueFormatter: ((Any) -> String)? = null
) {
    val formattedValue = valueFormatter?.invoke(value) ?: value.toString()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.alert_dialog_record_horizontal_space))
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dpToSp(R.dimen.employee_details_label_text_size),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.wrapContentWidth()
            )

            Text(
                text = formattedValue,
                color = MaterialTheme.colorScheme.primary,
                fontSize = dpToSp(R.dimen.employee_details_value_text_size),
                fontWeight = FontWeight.Bold,
                maxLines = maxLines,
                overflow = textOverflow,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
    }
}