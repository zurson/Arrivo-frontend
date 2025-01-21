package com.thesis.arrivo.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskToEdit
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.Location
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.changeActivity
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import kotlinx.coroutines.launch

class MainScaffoldViewModel(private val navigationManager: NavigationManager) : ViewModel(),
    LoadingScreenManager, LoggedInUserAccessor {

    companion object {
        fun reset() {
            FirebaseAuth.getInstance().signOut()

            changeActivity(
                context = MainActivity.context,
                destActivity = MainActivity::class,
                finish = true
            )
        }
    }

    private val loggedInUserDetailsViewModel = LoggedInUserDetailsViewModel(this)


    /**
     * Start App
     **/

    fun startApp() {
        // Show loading screen and initiate loading state
        showLoadingScreen()
        _appLoading.value = true

        // Check if the user is authenticated
        isUserAuthenticated { authStatus ->
            // Update the authentication state
            authenticated = authStatus
            println("Authentication status: $authenticated")

            if (!authenticated) {
                // If authentication fails, reset and stop the app initialization
                println("Authentication failed. Resetting...")
                authenticated = false
                _appLoading.value = false
                hideLoadingScreen()  // Hide loading screen after reset
                return@isUserAuthenticated  // Exit early to prevent further processing
            }

            // If authenticated, fetch logged-in user details
            fetchLoggedInUserDetails { userDetailsFetchingStatus ->
                if (!userDetailsFetchingStatus) {
                    // If fetching user details fails, reset and stop further execution
                    println("Failed to fetch user details. Resetting...")
                    reset()
                    _appLoading.value = false
                    hideLoadingScreen()  // Hide loading screen after reset
                    return@fetchLoggedInUserDetails  // Exit early to prevent further processing
                }

                println("SUCCES to fetch user details!")
                // If everything is successful, hide the loading screen and proceed
                _appLoading.value = false
                hideLoadingScreen()
            }
        }
    }


    /**
     * NavBar elements
     **/


    private val _appLoading = mutableStateOf(true)
    val appLoading: Boolean
        get() = _appLoading.value


    private var authenticated: Boolean = false


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
        if (isAdmin()) navbarElementsAdmin else navbarElementUser


    fun getStartDestination(): String {
        return when (authenticated) {
            true -> getNavbarElements().first().route
            false -> NavigationItem.Login.route
        }
    }


    private fun isUserAuthenticated(callback: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            callback(false)
            return
        }

        user.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                FirebaseAuth.getInstance().signOut()
                callback(false)
            }
        }
    }


    private fun fetchLoggedInUserDetails(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            loggedInUserDetailsViewModel.fetch { success ->
                callback(success)
            }
        }
    }


    /**
     * View selection
     **/

    private val _selectedView = mutableStateOf(getNavbarElements()[0])
    private val selectedView: NavigationItem
        get() = _selectedView.value

    private fun selectView(item: NavigationItem) {
        _selectedView.value = item
    }

    fun isSelected(item: NavigationItem): Boolean {
        return item.route == selectedView.route
    }

    fun onNavItemClick(clickedItem: NavigationItem) {
        if (isLoadingScreenEnabled())
            return

        if (clickedItem.route == selectedView.route)
            return

        navigationManager.navigateTo(clickedItem, true)
        selectView(clickedItem)
    }

    /**
     * Navbar visibility status
     **/

    private val _showNavbar = mutableStateOf(true)
    val showNavbar: Boolean
        get() = _showNavbar.value

    private fun setNavbarVisibility(visible: Boolean) {
        _showNavbar.value = visible
    }


    /**
     * Auth
     **/


    fun onAuthenticationSuccess() {
        fetchLoggedInUserDetails { success ->
            if (success) {
                authenticated = true
                setNavbarVisibility(true)
            } else {
                authenticated = false
                setNavbarVisibility(false)
            }

            navigationManager.navigateTo(getStartDestination(), true)
        }
    }


    fun manageNavbarOnLogin() {
        if (FirebaseAuth.getInstance().currentUser == null)
            setNavbarVisibility(false)
        else
            setNavbarVisibility(true)
    }


    /**
     * Employee Edit
     **/


    var employeeToEdit = Employee.emptyEmployee()


    /**
     * Employee Edit
     **/

    var taskToEdit: TaskToEdit = TaskToEdit(
        task = Task.emptyTask(),
        address = "",
        location = Location(0.0, 0.0)
    )


    /**
     * Loading Screen Manager
     **/

    private var loadingScreenEnabled by mutableStateOf(false)

    @Synchronized
    override fun showLoadingScreen() {
        loadingScreenEnabled = true
    }

    @Synchronized
    override fun hideLoadingScreen() {
        loadingScreenEnabled = false
    }

    override fun isLoadingScreenEnabled(): Boolean = loadingScreenEnabled


    /**
     * Logged In User Details Accessor
     **/


    override fun getLoggedInUserDetails(): Employee {
        return loggedInUserDetailsViewModel.getLoggedUserDetails()
    }

    override fun isAdmin(): Boolean {
        return loggedInUserDetailsViewModel.isAdmin()
    }


    /**
     * Other
     **/


}