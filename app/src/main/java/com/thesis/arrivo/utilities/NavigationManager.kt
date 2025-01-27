package com.thesis.arrivo.utilities

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.thesis.arrivo.communication.employee.Role
import com.thesis.arrivo.components.navigation.NavigationItem


class NavigationManager(private val navController: NavHostController) {


    /**
     * Role
     **/


    private var isAdmin: Boolean = false


    fun setRole(role: Role) {
        isAdmin = role == Role.ADMIN
    }


    /**
     * Bottom bar
     **/


    private val navbarElementUser = listOf(
        NavigationItem.TasksUser,
        NavigationItem.MapUser,
        NavigationItem.AccidentsUser,
        NavigationItem.ReportsUser,
        NavigationItem.AccountManagement,
    )

    private val navbarElementsAdmin = listOf(
        NavigationItem.AccidentsAdmin,
        NavigationItem.TasksListAdmin,
        NavigationItem.DeliveriesListAdmin,
        NavigationItem.EmployeesListAdmin,
        NavigationItem.AccountManagement
    )


    fun getNavbarElements(): List<NavigationItem> =
        if (isAdmin) navbarElementsAdmin else navbarElementUser


    private fun checkBottomBarSelectionChange(route: String) {
        if (!getNavbarElements().map { item -> item.route }.contains(route))
            return

        val matchingItem = getNavbarElements().find { it.route == route }

        if (matchingItem != null)
            selectView(matchingItem)
    }


    private val _selectedNavBarItem = mutableStateOf(getNavbarElements()[0])
    val selectedNavBarItem: NavigationItem
        get() = _selectedNavBarItem.value


    fun selectView(item: NavigationItem) {
        _selectedNavBarItem.value = item
    }


    /**
     * Navigation
     **/


    fun navigateTo(routeOrItem: Any, clearHistory: Boolean = false) {
        val route = when (routeOrItem) {
            is NavigationItem -> routeOrItem.route
            is String -> routeOrItem
            else -> throw IllegalArgumentException("Unsupported type for navigation")
        }

        runOnMainThread {
            navController.navigate(route) {
                if (clearHistory) {
                    popUpTo(0) { inclusive = true }
                }
            }

            checkBottomBarSelectionChange(route)
        }
    }


    fun navigateBack() {
        navController.popBackStack()
    }
}
