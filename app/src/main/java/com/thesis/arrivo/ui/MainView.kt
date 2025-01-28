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
import com.thesis.arrivo.ui.user.user_accident_report_view.AccidentReportView
import com.thesis.arrivo.ui.user.user_delivery_schedule_view.DeliveryScheduleView
import com.thesis.arrivo.ui.user.user_map_view.MapView
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.AccidentReportViewModel
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.DeliveriesListViewModel
import com.thesis.arrivo.view_models.DeliveryConfirmViewModel
import com.thesis.arrivo.view_models.DeliveryOptionsViewModel
import com.thesis.arrivo.view_models.DeliveryScheduleViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.EmployeeViewModel
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.RoadAccidentsAdminViewModel
import com.thesis.arrivo.view_models.RoadAccidentsUserViewModel
import com.thesis.arrivo.view_models.TaskManagerViewModel
import com.thesis.arrivo.view_models.TasksListViewModel
import com.thesis.arrivo.view_models.factory.AccidentReportViewModelFactory
import com.thesis.arrivo.view_models.factory.AuthViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveriesListViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryConfirmViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryOptionsViewModelFactory
import com.thesis.arrivo.view_models.factory.DeliveryScheduleViewModelFactory
import com.thesis.arrivo.view_models.factory.EmployeeViewModelFactory
import com.thesis.arrivo.view_models.factory.MainViewModelFactory
import com.thesis.arrivo.view_models.factory.RoadAccidentAdminViewModelFactory
import com.thesis.arrivo.view_models.factory.RoadAccidentsUserViewModelFactory
import com.thesis.arrivo.view_models.factory.TaskListViewModelFactory
import com.thesis.arrivo.view_models.factory.TaskManagerViewModelFactory


@Composable
fun MainView() {
    val navHostController = rememberNavController()
    val navigationManager = NavigationManager(navHostController)

    val deliverySharedViewModel: DeliverySharedViewModel = viewModel()

    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            context = LocalContext.current,
            navigationManager = navigationManager
        )
    )

    LaunchedEffect(key1 = true) {
        mainViewModel.startApp()
    }

    if (mainViewModel.appLoading) {
        LoadingScreen(true)
        return
    }

    SetupMainScaffold(
        navHostController = navHostController,
        mainViewModel = mainViewModel,
        navigationManager = navigationManager,
        deliverySharedViewModel = deliverySharedViewModel
    )
}


@Composable
private fun SetupMainScaffold(
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
    navigationManager: NavigationManager,
    deliverySharedViewModel: DeliverySharedViewModel
) {
    Theme.ArrivoTheme {
        MainScaffold(
            mainViewModel = mainViewModel
        ) { contentPadding ->
            NavHost(
                navController = navHostController,
                startDestination = mainViewModel.getStartDestination(),
                modifier = Modifier.padding(contentPadding)
            ) {
                setupUserViews(
                    mainViewModel = mainViewModel,
                    navigationManager = navigationManager
                )

                setupCommonViews(mainViewModel = mainViewModel)

                setupAdminViews(
                    mainViewModel = mainViewModel,
                    navigationManager = navigationManager,
                    deliverySharedViewModel = deliverySharedViewModel
                )

                setupAuthenticationViews(mainViewModel = mainViewModel)
            }
        }
        LoadingScreen(mainViewModel.isLoadingScreenEnabled())
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupUserViews(
    mainViewModel: MainViewModel,
    navigationManager: NavigationManager
) {
    composable(NavigationItem.TasksUser.route) {
        val vm: DeliveryScheduleViewModel = viewModel(
            factory = DeliveryScheduleViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainViewModel
            )
        )

        DeliveryScheduleView(vm)
    }

    composable(NavigationItem.MapUser.route) { MapView() }

    composable(NavigationItem.RoadAccidentsUser.route) {
        val viewModel: RoadAccidentsUserViewModel = viewModel(
            factory = RoadAccidentsUserViewModelFactory(
                loggedInUserAccessor = mainViewModel,
                context = LocalContext.current,
                loadingScreenManager = mainViewModel
            )
        )


        AccidentsListView(viewModel)
    }

    composable(NavigationItem.AccidentsReportsUser.route) {
        val viewModel: AccidentReportViewModel = viewModel(
            factory = AccidentReportViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainViewModel,
                loggedInUserAccessor = mainViewModel,
                navigationManager = navigationManager
            )
        )

        AccidentReportView(viewModel)
    }

    composable(NavigationItem.AccountManagement.route) { AccountView(mainViewModel) }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupAdminViews(
    mainViewModel: MainViewModel,
    navigationManager: NavigationManager,
    deliverySharedViewModel: DeliverySharedViewModel
) {
    composable(NavigationItem.AccidentsAdmin.route) {
        val viewModel: RoadAccidentsAdminViewModel = viewModel(
            factory = RoadAccidentAdminViewModelFactory(
                context = LocalContext.current,
                loadingScreenManager = mainViewModel
            )
        )

        AccidentsListView(roadAccidentsViewModel = viewModel)
    }

    composable(NavigationItem.TasksListAdmin.route) {
        val viewModel: TasksListViewModel = viewModel(
            factory = TaskListViewModelFactory(
                context = LocalContext.current,
                mainViewModel = mainViewModel,
                loadingScreenManager = mainViewModel,
                navigationManager = navigationManager,
            )
        )
        TasksListView(viewModel)
    }

    composable(NavigationItem.TaskCreateAdmin.route) {
        val viewModel: TaskManagerViewModel = viewModel(
            factory = TaskManagerViewModelFactory(
                mainViewModel = mainViewModel,
                navigationManager = navigationManager,
                loadingScreenManager = mainViewModel,
                context = LocalContext.current
            )
        )
        TaskCreateOrEditView(editMode = false, taskManagerViewModel = viewModel)
    }

    composable(NavigationItem.TaskEditAdmin.route) {
        val viewModel: TaskManagerViewModel = viewModel(
            factory = TaskManagerViewModelFactory(
                mainViewModel = mainViewModel,
                navigationManager = navigationManager,
                loadingScreenManager = mainViewModel,
                context = LocalContext.current
            )
        )
        TaskCreateOrEditView(editMode = true, taskManagerViewModel = viewModel)
    }

    composable(NavigationItem.EmployeesListAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainViewModel,
                mainViewModel = mainViewModel,
                context = LocalContext.current
            )
        )
        EmployeesListView(
            mainViewModel = mainViewModel,
            employeeViewModel = viewModel
        )
    }

    composable(NavigationItem.CreateEmployeeAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainViewModel,
                mainViewModel = mainViewModel,
                context = LocalContext.current
            )
        )
        val authVm: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                loadingScreenManager = mainViewModel,
                mainViewModel = mainViewModel,
            )
        )
        CreateEditEmployeeView(viewModel, authVm, editMode = false)
    }

    composable(NavigationItem.EditEmployeeAdmin.route) {
        val viewModel: EmployeeViewModel = viewModel(
            factory = EmployeeViewModelFactory(
                navigationManager = navigationManager,
                loadingScreenManager = mainViewModel,
                mainViewModel = mainViewModel,
                context = LocalContext.current
            )
        )
        val authVm: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                loadingScreenManager = mainViewModel,
                mainViewModel = mainViewModel,
            )
        )
        CreateEditEmployeeView(viewModel, authVm, editMode = true)
    }

    composable(NavigationItem.DeliveryOptionsAdmin.route) {
        val viewModel: DeliveryOptionsViewModel = viewModel(
            factory = DeliveryOptionsViewModelFactory(
                editMode = false,
                context = LocalContext.current,
                loadingScreenManager = mainViewModel,
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
                loadingScreenManager = mainViewModel,
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
                loadingScreenManager = mainViewModel,
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
                loadingScreenManager = mainViewModel,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            )
        )
        DeliveriesListView(deliveriesListViewModel = viewModel)
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupAuthenticationViews(mainViewModel: MainViewModel) {
    composable(NavigationItem.Login.route) {
        val viewModel: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                mainViewModel = mainViewModel,
                loadingScreenManager = mainViewModel
            )
        )
        LoginView(viewModel)
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
private fun NavGraphBuilder.setupCommonViews(mainViewModel: MainViewModel) {
    composable(NavigationItem.AccountManagement.route) {
        AccountView(mainViewModel)
    }
}
