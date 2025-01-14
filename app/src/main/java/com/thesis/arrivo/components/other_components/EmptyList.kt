package com.thesis.arrivo.components.other_components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker

@Composable
fun EmptyList(
    loadingScreenStatusChecker: LoadingScreenStatusChecker,
    @StringRes stringResourceId: Int = R.string.empty_list_text,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (loadingScreenStatusChecker.isLoadingScreenEnabled())
            return

        Icon(
            imageVector = Icons.Outlined.BrowserNotSupported,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.requiredSize(dimensionResource(R.dimen.empty_list_icon_size))
        )

        Text(
            text = stringResource(stringResourceId),
            fontSize = dpToSp(R.dimen.empty_list_text_size)
        )
    }
}