package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.thesis.arrivo.components.NavigationItem

class MainScaffoldViewModel : ViewModel() {

    /**
     * NavBar elements
     **/

    val navbarDestinations = listOf(
        NavigationItem.Tasks,
        NavigationItem.Map,
        NavigationItem.Accidents,
        NavigationItem.Reports,
        NavigationItem.Account
    )

    /**
     * View selection
     **/

    private val _selectedView = mutableStateOf(navbarDestinations[0])
    val selectedView: NavigationItem
        get() = _selectedView.value

    private fun selectView(item: NavigationItem) {
        if (item in navbarDestinations) {
            _selectedView.value = item
        }
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

    /**
     * Screen change animation
     **/
    fun screenChangeAnimation() {

    }

}