package com.thesis.arrivo.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.net.PlacesClient
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.components.other_components.LoadingScreen
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveriesListView
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveryCreateView
import com.thesis.arrivo.ui.admin.admin_deliveries.DeliveryTasksView
import com.thesis.arrivo.ui.admin.admin_employees.CreateEditEmployeeView
import com.thesis.arrivo.ui.admin.admin_employees.EmployeesListView
import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.TaskCreateOrEditView
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TasksListView
import com.thesis.arrivo.ui.authentication.LoginView
import com.thesis.arrivo.ui.common.account.AccountView
import com.thesis.arrivo.ui.common.road_accidents_list.AccidentsListView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.ui.user.user_delivery_view.DeliveryView
import com.thesis.arrivo.ui.user.user_map_view.MapView
import com.thesis.arrivo.ui.user.user_your_accidents_view.AccidentReportView
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.AccidentReportViewModel
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.DeliveriesListViewModel
import com.thesis.arrivo.view_models.DeliveryConfirmViewModel
import com.thesis.arrivo.view_models.DeliveryOptionsViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.EmployeeViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.RoadAccidentsAdminViewModel
import com.thesis.arrivo.view_models.RoadAccidentsUserViewModel
import com.thesis.arrivo.view_models.TaskManagerViewModel
import com.thesis.arrivo.view_models.TasksListViewModel
import com.thesis.arrivo.view_models.factory.AccidentReportViewModelFactory
import com.thesis.arrivo.view_models.factory.AuthViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveriesListViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryConfirmViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryOptionsViewModelFactory
import com.thesis.arrivo.view_models.factory.EmployeeViewModelFactory
import com.thesis.arrivo.view_models.factory.MainScaffoldViewModelFactory
import com.thesis.arrivo.view_models.factory.RoadAccidentAdminViewModelFactory
import com.thesis.arrivo.view_models.factory.RoadAccidentsUserViewModelFactory
import com.thesis.arrivo.view_models.factory.TaskListViewModelFactory
import com.thesis.arrivo.view_models.factory.TaskManagerViewModelFactory


@Composable
fun MainView() {
    val navHostController = rememberNavController()
    val navigationManager = NavigationManager(navHostController)

    val deliverySharedViewModel: DeliverySharedViewModel = viewModel()

    val mainScaffoldViewModel: MainScaffoldViewModel = viewModel(
        factory = MainScaffoldViewModelFactory(
            context = LocalContext.current,
            navigationManager = navigationManager
        )
    )

    LaunchedEffect(key1 = true) {
        mainScaffoldViewModel.startApp()
    }

    if (mainScaffoldViewModel.appLoading) {
        LoadingScreen(true)
        return
    }

    SetupMainScaffold(
        navHostController = navHostController,
        mainScaffoldViewModel = mainScaffoldViewModel,
        navigationManager = navigationManager,
        deliverySharedViewModel = deliverySharedViewModel
    )
}


@Composable
private fun SetupMainScaffold(
    navHostController: NavHostController,
    mainScaffoldViewModel: MainScaffoldViewModel,
    navigationManager: NavigationManager,
    deliverySharedViewModel: DeliverySharedViewModel
) {
    Theme.ArrivoTheme {
        MainScaffold(
            mainScaffoldViewModel = mainScaffoldViewModel
        ) { contentPadding ->
            NavHost(
                navController = navHostController,
                startDestination = mainScaffoldViewModel.getStartDestination(),
                modifier = Modifier.padding(contentPadding)
            ) {
                setupUserViews(mainScaffoldViewModel = mainScaffoldViewModel)

                setupCommonViews(mainScaffoldViewModel = mainScaffoldViewModel)

                setupAdminViews(
                    mainScaffoldViewModel = mainScaffoldViewModel,
                    navigationManager = navigationManager,
                    deliverySharedViewModel = deliverySharedViewModel
                )

                setupAuthenticationViews(mainScaffoldViewModel = mainScaffoldViewModel)
            }
        }
        LoadingScreen(mainScaffoldViewModel.isLoadingScreenEnabled())
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupUserViews(mainScaffoldViewModel: MainScaffoldViewModel) {
    composable(NavigationItem.TasksUser.route) { DeliveryView() }
    composable(NavigationItem.MapUser.route) { MapView() }

    composable(NavigationItem.AccidentsUser.route) {
        val viewModel: RoadAccidentsUserViewModel = viewModel(
            factory = RoadAccidentsUserViewModelFactory(
                loggedInUserAccessor = mainScaffoldViewModel,
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel
            )
        )


        AccidentsListView(viewModel)
    }

    composable(NavigationItem.ReportsUser.route) {
        val viewModel: AccidentReportViewModel = viewModel(
            factory = AccidentReportViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel
            )
        )

        AccidentReportView(viewModel)
    }

    composable(NavigationItem.AccountManagement.route) { AccountView(mainScaffoldViewModel) }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupAdminViews(
    mainScaffoldViewModel: MainScaffoldViewModel,
    navigationManager: NavigationManager,
    deliverySharedViewModel: DeliverySharedViewModel
) {
    composable(NavigationItem.AccidentsAdmin.route) {
        val viewModel: RoadAccidentsAdminViewModel = viewModel(
            factory = RoadAccidentAdminViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel
            )
        )

        AccidentsListView(roadAccidentsViewModel = viewModel)
    }

    composable(NavigationItem.TasksListAdmin.route) {
        val viewModel: TasksListViewModel = viewModel(
            factory = TaskListViewModelFactory(
                context = LocalContext.current,
                mainScaffoldViewModel = mainScaffoldViewModel,
                loadingScreenManager = mainScaffoldViewModel,
                navigationManager = navigationManager,
            )
        )
        TasksListView(viewModel)
    }

    composable(NavigationItem.TaskCreateAdmin.route) {
        val viewModel: TaskManagerViewModel = viewModel(
            factory = TaskManagerViewModelFactory(
                mainScaffoldViewModel = mainScaffoldViewModel,
                navigationManager = navigationManager,
                loadingScreenManager = mainScaffoldViewModel,
                context = LocalContext.current
            )
        )
        TaskCreateOrEditView(editMode = false, taskManagerViewModel = viewModel)
    }

    composable(NavigationItem.TaskEditAdmin.route) {
        val viewModel: TaskManagerViewModel = viewModel(
            factory = TaskManagerViewModelFactory(
                mainScaffoldViewModel = mainScaffoldViewModel,
                navigationManager = navigationManager,
                loadingScreenManager = mainScaffoldViewModel,
                context = LocalContext.current
            )
        )
        TaskCreateOrEditView(editMode = true, taskManagerViewModel = viewModel)
    }

    composable(NavigationItem.EmployeesListAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainScaffoldViewModel,
                mainScaffoldViewModel = mainScaffoldViewModel,
                context = LocalContext.current
            )
        )
        EmployeesListView(
            mainScaffoldViewModel = mainScaffoldViewModel,
            employeeViewModel = viewModel
        )
    }

    composable(NavigationItem.CreateEmployeeAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainScaffoldViewModel,
                mainScaffoldViewModel = mainScaffoldViewModel,
                context = LocalContext.current
            )
        )
        val authVm: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                loadingScreenManager = mainScaffoldViewModel,
                mainScaffoldViewModel = mainScaffoldViewModel,
            )
        )
        CreateEditEmployeeView(viewModel, authVm, editMode = false)
    }

    composable(NavigationItem.EditEmployeeAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainScaffoldViewModel,
                mainScaffoldViewModel = mainScaffoldViewModel,
                context = LocalContext.current
            )
        )
        val authVm: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                loadingScreenManager = mainScaffoldViewModel,
                mainScaffoldViewModel = mainScaffoldViewModel,
            )
        )
        CreateEditEmployeeView(viewModel, authVm, editMode = true)
    }

    composable(NavigationItem.DeliveryOptionsAdmin.route) {
        val viewModel: DeliveryOptionsViewModel = viewModel(
            factory = DeliveryOptionsViewModelFactory(
                editMode = false,
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            )
        )
        DeliveryTasksView(deliveryOptionsViewModel = viewModel)
    }

    composable(NavigationItem.DeliveryOptionsEditAdmin.route) {
        val viewModel: DeliveryOptionsViewModel = viewModel(
            factory = DeliveryOptionsViewModelFactory(
                editMode = true,
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            )
        )
        DeliveryTasksView(deliveryOptionsViewModel = viewModel)
    }

    composable(NavigationItem.DeliveryConfirmAdmin.route) {
        val viewModel: DeliveryConfirmViewModel = viewModel(
            factory = DeliveryConfirmViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            )
        )
        DeliveryCreateView(deliveryConfirmViewModel = viewModel)
    }

    composable(NavigationItem.DeliveriesListAdmin.route) {
        val viewModel: DeliveriesListViewModel = viewModel(
            factory = DeliveriesListViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainScaffoldViewModel,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            )
        )
        DeliveriesListView(deliveriesListViewModel = viewModel)
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupAuthenticationViews(mainScaffoldViewModel: MainScaffoldViewModel) {
    composable(NavigationItem.Login.route) {
        val viewModel: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                mainScaffoldViewModel = mainScaffoldViewModel,
                loadingScreenManager = mainScaffoldViewModel
            )
        )
        LoginView(viewModel)
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupCommonViews(mainScaffoldViewModel: MainScaffoldViewModel) {
    composable(NavigationItem.AccountManagement.route) {
        AccountView(mainScaffoldViewModel)
    }
}
