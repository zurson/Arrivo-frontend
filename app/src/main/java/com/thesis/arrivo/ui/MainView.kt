package com.thesis.arrivo.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.components.MainScaffold
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.ui.admin.admin_accidents.AccidentsView
import com.thesis.arrivo.ui.admin.admin_employees.EmployeesView
import com.thesis.arrivo.ui.admin.admin_tasks.TasksView
import com.thesis.arrivo.ui.user.user_account_view.AccountView
import com.thesis.arrivo.ui.user.user_delivery_view.DeliveryView
import com.thesis.arrivo.ui.user.user_map_view.MapView
import com.thesis.arrivo.ui.user.user_road_accident_view.RoadAccidentView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.ui.user.user_your_accidents_view.YourAccidentsView
import com.thesis.arrivo.view_models.MainScaffoldViewModel

@Composable
fun MainView() {
    Theme.ArrivoTheme {
        val navHostController = rememberNavController()
        val mainScaffoldViewModel = MainScaffoldViewModel(true)

        MainScaffold(
            navHostController = navHostController,
            mainScaffoldViewModel = mainScaffoldViewModel
        ) { contentPadding ->

            Log.e("AAAAAA", mainScaffoldViewModel.getNavbarElements().first().route)

            NavHost(
                navController = navHostController,
                startDestination = mainScaffoldViewModel.getNavbarElements().first().route,
                modifier = Modifier.padding(contentPadding)
            ) {
                /** User **/
                composable(NavigationItem.TasksUser.route) { DeliveryView() }
                composable(NavigationItem.MapUser.route) { MapView() }
                composable(NavigationItem.AccidentsUser.route) { RoadAccidentView() }
                composable(NavigationItem.ReportsUser.route) { YourAccidentsView() }
                composable(NavigationItem.AccountUser.route) { AccountView() }

                /** Admin **/
                composable(NavigationItem.AccidentsAdmin.route) { AccidentsView() }
                composable(NavigationItem.TasksAdmin.route) { TasksView() }
                composable(NavigationItem.EmployeesAdmin.route) { EmployeesView() }
            }

        }
    }
}