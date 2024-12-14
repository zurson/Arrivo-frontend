package com.thesis.arrivo.view_models

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.utilities.navigateTo

class MainScaffoldViewModel(
    var adminMode: Boolean,
    val navController: NavHostController
) : ViewModel() {

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


    private fun isUserAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getStartDestination(): NavigationItem {
        return if (!isUserAuthenticated()) {
            NavigationItem.Login
        } else {
            getNavbarElements().first()
        }
    }

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

    fun onNavItemClick(clickedItem: NavigationItem) {
        if (clickedItem.route == selectedView.route)
            return

        navController.navigate(clickedItem.route) {
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
     * Login success
     **/

    fun onAuthenticationSuccess() {
        Handler(Looper.getMainLooper()).post({
            navigateTo(navController, getStartDestination())
            setNavbarVisibility(true)
        })
    }


    fun manageNavbarOnLogin() {
        if (FirebaseAuth.getInstance().currentUser == null)
            setNavbarVisibility(false)
        else
            setNavbarVisibility(true)
    }


    /**
     * Other
     **/

    fun onCreateEmployeeAccountRedirectButtonClick() {
        navigateTo(
            navController = navController,
            navigationItem = NavigationItem.CreateEmployeeAdmin
        )
    }

}