package com.thesis.arrivo.ui.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.thesis.arrivo.R
import com.thesis.arrivo.components.bounceClick

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedButton(
        shape = RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .bounceClick(),
        onClick = onClick,
    ) {
        content()
    }
}