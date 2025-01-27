package com.thesis.arrivo.components.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.MainScaffoldViewModel


@Composable
fun NavBar(mainScaffoldViewModel: MainScaffoldViewModel) {
    val navItems = mainScaffoldViewModel.getNavbarElements()

    AppNavigationBar(
        mainScaffoldViewModel = mainScaffoldViewModel,
        navItems = navItems
    )
}


@Composable
fun AppNavigationBar(
    mainScaffoldViewModel: MainScaffoldViewModel,
    navItems: List<NavigationItem>
) {
    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(modifier = Modifier
                .wrapContentHeight()
                .padding(top = dimensionResource(R.dimen.navbar_top_padding)),

                label = {
                    item.title?.let {
                        Text(
                            text = stringResource(it),
                            fontSize = dpToSp(R.dimen.navbar_text_size),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                alwaysShowLabel = true,
                selected = mainScaffoldViewModel.isViewSelected(item),

                icon = {
                    item.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.navbar_icon_size))
                        )
                    }

                },

                onClick = { mainScaffoldViewModel.onNavItemClick(item) })

        }
    }
}