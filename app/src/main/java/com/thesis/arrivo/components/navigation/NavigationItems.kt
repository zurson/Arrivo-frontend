package com.thesis.arrivo.components.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DeliveryDining
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FormatListNumberedRtl
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector
import com.thesis.arrivo.R

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int? = null,
    val icon: ImageVector? = null,
) {

    /** User **/

    data object TasksUser : NavigationItem(
        route = "delivery_schedule_user",
        title = R.string.nav_delivery_schedule_label_user,
        icon = Icons.Outlined.DirectionsCar
    )

    data object MapUser : NavigationItem(
        route = "map_user",
        title = R.string.nav_map_label_user,
        icon = Icons.Outlined.LocationOn
    )

    data object RoadAccidentsUser : NavigationItem(
        route = "accidents_user",
        title = R.string.nav_road_accident_label_user,
        icon = Icons.Outlined.ReportProblem
    )

    data object AccidentsReportsUser : NavigationItem(
        route = "reports_user",
        title = R.string.nav_your_reports_label_user,
        icon = Icons.Outlined.FormatListNumberedRtl
    )

    /** Common **/

    data object AccountManagement : NavigationItem(
        route = "account_management",
        title = R.string.nav_account_management_label,
        icon = Icons.Outlined.AccountCircle
    )

    /** Admin **/

    data object AccidentsAdmin : NavigationItem(
        route = "accidents_admin",
        title = R.string.nav_accidents_label_admin,
        icon = Icons.Outlined.ReportProblem
    )

    data object TasksListAdmin : NavigationItem(
        route = "tasks_list_admin",
        title = R.string.nav_tasks_list_label_admin,
        icon = Icons.Outlined.Task
    )

    data object EmployeesListAdmin : NavigationItem(
        route = "employees_list_admin",
        title = R.string.nav_employees_label_admin,
        icon = Icons.Outlined.Person
    )

    data object DeliveriesListAdmin : NavigationItem(
        route = "deliveries_list_admin",
        title = R.string.nav_deliveries_label_admin,
        icon = Icons.Outlined.DeliveryDining
    )

    data object TaskCreateAdmin : NavigationItem(
        route = "task_create_admin",
    )

    data object TaskEditAdmin : NavigationItem(
        route = "task_edit_admin",
    )

    data object CreateEmployeeAdmin : NavigationItem(
        route = "create_employee_admin"
    )

    data object EditEmployeeAdmin : NavigationItem(
        route = "edit_employee_admin"
    )

    data object DeliveryOptionsAdmin : NavigationItem(
        route = "delivery_option_admin"
    )

    data object DeliveryOptionsEditAdmin : NavigationItem(
        route = "delivery_options_edit_admin"
    )

    data object DeliveryConfirmAdmin : NavigationItem(
        route = "delivery_confirm_admin"
    )

    data object WorkTimeAdmin : NavigationItem(
        route = "work_time_admin"
    )

    /** Login **/

    data object Login : NavigationItem(
        route = "login",
    )
}