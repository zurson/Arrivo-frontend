package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AlertDialogLazyColumn(
    title: String,
    items: List<@Composable () -> Unit>,
    listHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = dpToSp(R.dimen.dialog_record_label_text_size),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            modifier = Modifier.height(listHeight)
        ) {
            items(items.size) { index ->
                items[index]()
            }
        }
    }
}