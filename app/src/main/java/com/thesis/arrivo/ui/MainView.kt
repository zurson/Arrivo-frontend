package com.thesis.arrivo.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.net.PlacesClient
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.components.other_components.LoadingScreen
import com.thesis.arrivo.ui.admin.admin_accidents.AccidentsView
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveriesListView
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveryCreateView
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveryTasksView
import com.thesis.arrivo.ui.admin.admin_employees.CreateEditEmployeeView
import com.thesis.arrivo.ui.admin.admin_employees.EmployeesListView
import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.TaskCreateOrEditView
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TasksListView
import com.thesis.arrivo.ui.authentication.LoginView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.ui.user.user_account_view.AccountView
import com.thesis.arrivo.ui.user.user_delivery_view.DeliveryView
import com.thesis.arrivo.ui.user.user_map_view.MapView
import com.thesis.arrivo.ui.user.user_road_accident_view.RoadAccidentView
import com.thesis.arrivo.ui.user.user_your_accidents_view.YourAccidentsView
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.DeliveriesListViewModel
import com.thesis.arrivo.view_models.DeliveryCreateViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.DeliveryTaskSelectViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.DeliveriesListViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryCreateViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryTaskSelectViewModelFactory
import com.thesis.arrivo.view_models.factory.MainScaffoldViewModelFactory


@Composable
fun MainView(placesClient: PlacesClient) {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val navigationManager = NavigationManager(navHostController)

    val mainScaffoldViewModel: MainScaffoldViewModel = viewModel(
        factory = MainScaffoldViewModelFactory(
            context = context,
            adminMode = true,
            navigationManager = navigationManager
        )
    )

    SetupMainScaffold(
        placesClient = placesClient,
        navHostController = navHostController,
        mainScaffoldViewModel = mainScaffoldViewModel,
        navigationManager = navigationManager
    )
}


@Composable
private fun SetupMainScaffold(
    placesClient: PlacesClient,
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel,
    navigationManager: NavigationManager
) {
    var startDestination by remember { mutableStateOf<NavigationItem?>(null) }

    LaunchedEffect(Unit) {
        mainScaffoldViewModel.getStartDestination { dest ->
            startDestination = dest
        }
    }

    if (startDestination == null) {
        mainScaffoldViewModel.showLoadingScreen()
        return
    }
    mainScaffoldViewModel.hideLoadingScreen()

    /**
     * SHARED VIEW MODELS
     **/

    val deliverySharedViewModel: DeliverySharedViewModel = viewModel()

    Theme.ArrivoTheme {
        MainScaffold(
            mainScaffoldViewModel = mainScaffoldViewModel
        ) { contentPadding ->
            NavHost(
                navController = navHostController,
                startDestination = startDestination!!.route,
                modifier = Modifier.padding(contentPadding)
            ) {
                /** User **/
                composable(NavigationItem.TasksUser.route) { DeliveryView() }
                composable(NavigationItem.MapUser.route) { MapView() }
                composable(NavigationItem.AccidentsUser.route) { RoadAccidentView() }
                composable(NavigationItem.ReportsUser.route) { YourAccidentsView() }
                composable(NavigationItem.AccountUser.route) { AccountView() }

                /** Admin **/
                composable(NavigationItem.AccidentsAdmin.route) {
                    AccidentsView(loadingScreenManager = mainScaffoldViewModel)
                }

                composable(NavigationItem.TasksListAdmin.route) {
                    TasksListView(
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        loadingScreenManager = mainScaffoldViewModel,
                        navigationManager = navigationManager
                    )
                }

                composable(NavigationItem.TaskCreateAdmin.route) {
                    TaskCreateOrEditView(
                        placesClient = placesClient,
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        editMode = false,
                        navigationManager = navigationManager,
                        loadingScreenManager = mainScaffoldViewModel
                    )
                }

                composable(NavigationItem.TaskEditAdmin.route) {
                    TaskCreateOrEditView(
                        placesClient = placesClient,
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        editMode = true,
                        navigationManager = navigationManager,
                        loadingScreenManager = mainScaffoldViewModel
                    )
                }

                composable(NavigationItem.EmployeesListAdmin.route) {
                    EmployeesListView(
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        loadingScreenManager = mainScaffoldViewModel,
                        navigationManager = navigationManager
                    )
                }

                composable(NavigationItem.CreateEmployeeAdmin.route) {
                    CreateEditEmployeeView(
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        editMode = false,
                        navigationManager = navigationManager,
                        loadingScreenManager = mainScaffoldViewModel
                    )
                }

                composable(NavigationItem.EditEmployeeAdmin.route) {
                    CreateEditEmployeeView(
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        editMode = true,
                        navigationManager = navigationManager,
                        loadingScreenManager = mainScaffoldViewModel
                    )
                }

                composable(NavigationItem.DeliveryTasksAdmin.route) {
                    val viewModel: DeliveryTaskSelectViewModel = viewModel(
                        factory = DeliveryTaskSelectViewModelFactory(
                            context = LocalContext.current,
                            loadingScreenManager = mainScaffoldViewModel,
                            navigationManager = navigationManager,
                            deliverySharedViewModel = deliverySharedViewModel
                        )
                    )

                    DeliveryTasksView(deliveryTaskSelectViewModel = viewModel)
                }

                composable(NavigationItem.DeliveryCreateAdmin.route) {
                    val viewModel: DeliveryCreateViewModel = viewModel(
                        factory = DeliveryCreateViewModelFactory(
                            context = LocalContext.current,
                            loadingScreenManager = mainScaffoldViewModel,
                            navigationManager = navigationManager,
                            deliverySharedViewModel = deliverySharedViewModel
                        )
                    )

                    DeliveryCreateView(deliveryCreateViewModel = viewModel)
                }

                composable(NavigationItem.DeliveriesListAdmin.route) {
                    val viewModel: DeliveriesListViewModel = viewModel(
                        factory = DeliveriesListViewModelFactory(
                            context = LocalContext.current,
                            loadingScreenManager = mainScaffoldViewModel,
                            navigationManager = navigationManager,
                        )
                    )

                    DeliveriesListView(deliveriesListViewModel = viewModel)
                }

                /** Authentication **/
                composable(NavigationItem.Login.route) {
                    LoginView(
                        mainScaffoldViewModel = mainScaffoldViewModel,
                        loadingScreenManager = mainScaffoldViewModel
                    )
                }
            }

        }

        LoadingScreen(mainScaffoldViewModel.isLoadingScreenEnabled())
        mainScaffoldViewModel.startAuthListeners()
    }
}