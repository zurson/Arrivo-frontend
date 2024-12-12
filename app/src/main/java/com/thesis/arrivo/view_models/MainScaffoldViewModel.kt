package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.thesis.arrivo.components.NavigationItem

class MainScaffoldViewModel(var adminMode: Boolean) : ViewModel() {

    /**
     * NavBar elements
     **/

    private val navbarElementUser = listOf(
        NavigationItem.TasksUser,
        NavigationItem.MapUser,
        NavigationItem.AccidentsUser,
        NavigationItem.ReportsUser,
        NavigationItem.AccountUser
    )

    private val navbarElementsAdmin = listOf(
        NavigationItem.AccidentsAdmin,
        NavigationItem.TasksAdmin,
        NavigationItem.EmployeesAdmin,
    )

    fun getNavbarElements(): List<NavigationItem> =
        if (adminMode) navbarElementsAdmin else navbarElementUser

    /**
     * View selection
     **/

    private val _selectedView = mutableStateOf(getNavbarElements()[0])
    val selectedView: NavigationItem
        get() = _selectedView.value

    private fun selectView(item: NavigationItem) {
        _selectedView.value = item
    }

    fun isSelected(item: NavigationItem): Boolean {
        return item.route == selectedView.route
    }

    fun onNavItemClick(clickedItem: NavigationItem, navHostController: NavHostController) {
        if (clickedItem.route == selectedView.route)
            return

        navHostController.navigate(clickedItem.route) {
            popUpTo(selectedView.route) { inclusive = true }
            launchSingleTop = true
        }

        selectView(clickedItem)
    }

    /**
     * Navbar visibility status
     **/

    private val _showNavbar = mutableStateOf(true)
    val showNavbar: Boolean
        get() = _showNavbar.value

    fun setNavbarVisibility(visible: Boolean) {
        _showNavbar.value = visible
    }

}