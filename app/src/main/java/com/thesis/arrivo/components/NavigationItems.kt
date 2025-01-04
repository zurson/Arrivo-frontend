package com.thesis.arrivo.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
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
        route = "tasks_user",
        title = R.string.nav_delivery_details_label_user,
        icon = Icons.Outlined.DirectionsCar
    )

    data object MapUser : NavigationItem(
        route = "map_user",
        title = R.string.nav_map_label_user,
        icon = Icons.Outlined.LocationOn
    )

    data object AccidentsUser : NavigationItem(
        route = "accidents_user",
        title = R.string.nav_road_accident_label_user,
        icon = Icons.Outlined.ReportProblem
    )

    data object ReportsUser : NavigationItem(
        route = "reports_user",
        title = R.string.nav_your_reports_label_user,
        icon = Icons.Outlined.FormatListNumberedRtl
    )

    data object AccountUser : NavigationItem(
        route = "account_user",
        title = R.string.nav_help_label_user,
        icon = Icons.Outlined.AccountCircle
    )

    /** Admin **/

    data object AccidentsAdmin : NavigationItem(
        route = "accidents_admin",
        title = R.string.nav_accidents_label_admin,
        icon = Icons.Outlined.ReportProblem
    )

    data object TasksAdmin : NavigationItem(
        route = "tasks_admin",
        title = R.string.nav_tasks_label_admin,
        icon = Icons.Outlined.Task
    )

    data object EmployeesAdmin : NavigationItem(
        route = "employees_admin",
        title = R.string.nav_employees_label_admin,
        icon = Icons.Outlined.Person
    )

    data object CreateEmployeeAdmin : NavigationItem(
        route = "create_employee_admin"
    )

    data object EditEmployeeAdmin : NavigationItem(
        route = "edit_employee_admin"
    )

    /** Login **/

    data object Login : NavigationItem(
        route = "login",
    )
}