package com.thesis.arrivo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.thesis.arrivo.R
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.dpToSp

@Composable
fun AppButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (textRef, iconRef) = createRefs()
            val separatorGuideline = createGuidelineFromStart(if (icon != null) 0.90f else 1f)

            Text(
                text = text,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontSize = dpToSp(R.dimen.app_button_text_size),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .constrainAs(textRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(separatorGuideline)
                        width = Dimension.fillToConstraints
//                    height = Dimension.fillToConstraints
                    }
                    .padding(end = 4.dp)
            )

            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(iconRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(separatorGuideline)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
//                            height = Dimension.fillToConstraints
                        }
                        .requiredSize(dimensionResource(R.dimen.app_button_icon_size)),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    }
}


@Preview
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
        AppButton(
            onClick = {},
            text = "Create account",
            icon = Icons.Outlined.Close
        )
    }
}