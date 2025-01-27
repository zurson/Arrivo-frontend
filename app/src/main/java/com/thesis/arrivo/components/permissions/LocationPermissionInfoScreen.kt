package com.thesis.arrivo.components.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.utilities.Settings

@Composable
fun LocationPermissionInfoScreen(onOpenSettingsClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        val contentRef = createRef()

        val topGuideline = createGuidelineFromTop(Settings.START_END_PERCENTAGE)
        val bottomGuideline = createGuidelineFromBottom(Settings.START_END_PERCENTAGE)
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        Box(
            modifier = Modifier
                .constrainAs(contentRef) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(topGuideline)
                    bottom.linkTo(bottomGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space))
            ) {
                Text(
                    text = stringResource(id = R.string.location_permission_required),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.location_permission_message),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.grant_permissions_instructions),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left,
                )

                AppButton(
                    onClick = onOpenSettingsClick,
                    text = stringResource(id = R.string.open_settings)
                )
            }
        }
    }
}