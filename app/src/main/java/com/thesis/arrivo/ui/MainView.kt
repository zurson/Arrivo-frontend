package com.thesis.arrivo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.net.PlacesClient
import com.thesis.arrivo.components.MainScaffold
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.ui.admin.admin_accidents.AccidentsView
import com.thesis.arrivo.ui.admin.admin_employees.CreateEditEmployeeView
import com.thesis.arrivo.ui.admin.admin_employees.EmployeesView
import com.thesis.arrivo.ui.admin.admin_tasks.create_task.TasksView
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TasksListView
import com.thesis.arrivo.ui.authentication.LoginView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.ui.user.user_account_view.AccountView
import com.thesis.arrivo.ui.user.user_delivery_view.DeliveryView
import com.thesis.arrivo.ui.user.user_map_view.MapView
import com.thesis.arrivo.ui.user.user_road_accident_view.RoadAccidentView
import com.thesis.arrivo.ui.user.user_your_accidents_view.YourAccidentsView
import com.thesis.arrivo.view_models.MainScaffoldViewModel


@Composable
fun MainView(placesClient: PlacesClient) {
    val context = LocalContext.current
    val navHostController = rememberNavController()

    val mainScaffoldViewModel = MainScaffoldViewModel(
        context = context,
        adminMode = true,
        navController = navHostController
    )

    SetupMainScaffold(
        placesClient = placesClient,
        navHostController = navHostController,
        mainScaffoldViewModel = mainScaffoldViewModel
    )
}


@Composable
private fun SetupMainScaffold(
    placesClient: PlacesClient,
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel
) {
    var destLoaded by remember { mutableStateOf(false) }
    var startDestination: NavigationItem = NavigationItem.Login

    mainScaffoldViewModel.getStartDestination { dest ->
        startDestination = dest
        destLoaded = true
    }

    Theme.ArrivoTheme {
        MainScaffold(
            mainScaffoldViewModel = mainScaffoldViewModel
        ) { contentPadding ->

            if (destLoaded) {
                NavHost(
                    navController = navHostController,
                    startDestination = startDestination.route,
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
                    composable(NavigationItem.TasksAdmin.route) {
                        /*TasksView(
                            placesClient = placesClient,
                            navHostController = navHostController
                        )*/
                        TasksListView(navHostController)
                    }

                    composable(NavigationItem.EmployeesAdmin.route) {
                        EmployeesView(mainScaffoldViewModel)
                    }

                    composable(NavigationItem.CreateEmployeeAdmin.route) {
                        CreateEditEmployeeView(mainScaffoldViewModel, editMode = false)
                    }

                    composable(NavigationItem.EditEmployeeAdmin.route) {
                        CreateEditEmployeeView(mainScaffoldViewModel, editMode = true)
                    }

                    /** Authentication **/
                    composable(NavigationItem.Login.route) {
                        LoginView(mainScaffoldViewModel = mainScaffoldViewModel)
                    }
                }
            }

            mainScaffoldViewModel.startAuthListeners()
        }
    }
}
