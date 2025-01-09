package com.thesis.arrivo.components.info_alert_dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thesis.arrivo.R
import com.thesis.arrivo.components.bounceClick
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.dpToSp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = { onDismiss() }, modifier = modifier.wrapContentSize()
    ) {

        Column(
            modifier = Modifier
                .width(dimensionResource(R.dimen.alert_dialog_window_width))
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius))
                )
                .border(
                    BorderStroke(5.dp, MaterialTheme.colorScheme.primary),
                    RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius))
                )
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                val exitIconPadding = dimensionResource(R.dimen.alert_dialog_exit_icon_padding)

                Icon(imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .bounceClick()
                        .padding(end = exitIconPadding, top = exitIconPadding)
                        .clickable { onDismiss() }
                        .size(dimensionResource(R.dimen.alert_dialog_close_icon_size)))
            }

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = dpToSp(R.dimen.alert_dialog_title_font_size),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )

            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                content()
            }

        }


    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowPreviewHistoryDetailsDialog() {
    Theme.ArrivoTheme {
        InfoAlertDialog(modifier = Modifier, title = "Test title", onDismiss = { }) {
            repeat(10) {
                DialogRecord(
                    label = "Label", value = "Value"
                )
            }
        }
    }
}