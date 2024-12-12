package com.thesis.arrivo.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.MainScaffoldViewModel


@Composable
fun NavBar(
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel
) {
    val containerColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary
    val navItems = mainScaffoldViewModel.getNavbarElements()

    AppNavigationBar(
        navHostController = navHostController,
        mainScaffoldViewModel = mainScaffoldViewModel,
        containerColor = containerColor,
        contentColor = contentColor,
        navItems = navItems
    )
}


@Composable
fun AppNavigationBar(
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel,
    contentColor: Color,
    containerColor: Color,
    navItems: List<NavigationItem>
) {

    NavigationBar(
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        navItems.forEach { item ->
            val selected = mainScaffoldViewModel.isSelected(item)
            NavigationBarItem(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(top = dimensionResource(R.dimen.navbar_top_padding)),
                label = {
                    Text(
                        text = stringResource(item.title),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = dpToSp(R.dimen.navbar_text_size),
                        textAlign = TextAlign.Center
                    )
                },
                alwaysShowLabel = true,
                selected = selected,
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.surface,
                    disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                    disabledTextColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.navbar_icon_size))
                    )
                },
                onClick = {
                    mainScaffoldViewModel.onNavItemClick(
                        navHostController = navHostController,
                        clickedItem = item
                    )
                }
            )

        }
    }
}