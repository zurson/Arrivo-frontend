package com.thesis.arrivo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.components.MainScaffold
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.ui.account_view.AccountView
import com.thesis.arrivo.ui.delivery_view.DeliveryView
import com.thesis.arrivo.ui.map_view.MapView
import com.thesis.arrivo.ui.road_accident_view.RoadAccidentView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.ui.your_accidents_view.YourAccidentsView
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun MainView() {
    Theme.ArrivoTheme {
        val navHostController = rememberNavController()
        val mainScaffoldViewModel = MainScaffoldViewModel()

        MainScaffold(
            navHostController = navHostController,
            mainScaffoldViewModel = mainScaffoldViewModel
        ) { contentPadding ->

            NavHost(
                navController = navHostController,
                startDestination = mainScaffoldViewModel.navbarDestinations.first().route,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(NavigationItem.Tasks.route) { DeliveryView() }
                composable(NavigationItem.Map.route) { MapView() }
                composable(NavigationItem.Accidents.route) { RoadAccidentView() }
                composable(NavigationItem.Reports.route) { YourAccidentsView() }
                composable(NavigationItem.Account.route) { AccountView() }
            }

        }
    }
}