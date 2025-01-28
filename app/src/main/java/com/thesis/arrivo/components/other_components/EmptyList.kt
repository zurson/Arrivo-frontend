package com.thesis.arrivo.components.other_components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrowserNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker

@Composable
fun EmptyList(
    loadingScreenStatusChecker: LoadingScreenStatusChecker,
    @StringRes stringResourceId: Int = R.string.empty_list_text,
    icon: ImageVector = Icons.Outlined.BrowserNotSupported,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val ref = createRef()
        val startGuideLine = createGuidelineFromStart(0.1f)
        val endGuideLine = createGuidelineFromEnd(0.1f)

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(ref) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(startGuideLine)
                    end.linkTo(endGuideLine)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
        ) {
            if (loadingScreenStatusChecker.isLoadingScreenEnabled())
                return@Column

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.requiredSize(dimensionResource(R.dimen.empty_list_icon_size))
            )

            Text(
                text = stringResource(stringResourceId),
                fontSize = dpToSp(R.dimen.empty_list_text_size),
                textAlign = TextAlign.Center
            )
        }
    }
}