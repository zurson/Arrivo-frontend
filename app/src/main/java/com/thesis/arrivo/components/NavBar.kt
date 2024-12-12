package com.thesis.arrivo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun NavBar(
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel
) {

    val items = mainScaffoldViewModel.navbarDestinations

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        items.forEach { item ->
            val selected = mainScaffoldViewModel.isSelected(item)
            val imageVector = mainScaffoldViewModel.getIcon(item, selected)

            NavigationBarItem(
                modifier = Modifier.wrapContentHeight(),
                label = {
                    Text(
                        text = stringResource(item.title),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
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
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
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