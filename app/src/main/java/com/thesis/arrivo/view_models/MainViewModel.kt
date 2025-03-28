package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.R
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskToEdit
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.changeActivity
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.showDefaultErrorDialog
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock

class MainViewModel(
    private val context: Context,
    private val navigationManager: NavigationManager
) : ViewModel(),
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

    private val loggedInUserDetailsViewModel = LoggedInUserDetailsViewModel(context, this)


    /**
     * Start App
     **/

    fun startApp() {
        _appLoading.value = true

        isUserAuthenticated { authStatus ->
            authenticated = authStatus

            if (!authStatus) {
                appStartFinish()
                return@isUserAuthenticated
            }

            fetchLoggedInUserDetails { userDetailsFetchingStatus ->
                if (!userDetailsFetchingStatus) {
                    appStartFinish()
                    setNavbarVisibility(false)
                    reset()
                    return@fetchLoggedInUserDetails
                }

                navigationManager.setRole(getLoggedInUser().role)
                navigationManager.selectView(getNavbarElements().first())
                setNavbarVisibility(true)
                _appLoading.value = false
            }
        }
    }


    private fun showUserDetailsFetchFailAlertBox() {
        showDefaultErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            message = context.getString(R.string.unexpected_error)
        )
    }


    private fun appStartFinish() {
        authenticated = false
        _appLoading.value = false
    }


    /**
     * NavBar
     **/


    private val _appLoading = mutableStateOf(true)
    val appLoading: Boolean
        get() = _appLoading.value


    private var authenticated: Boolean = false


    fun getNavbarElements(): List<NavigationItem> = navigationManager.getNavbarElements()


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


    fun onNavItemClick(clickedItem: NavigationItem) {
        if (isLoadingScreenEnabled())
            return

        if (clickedItem.route == navigationManager.selectedNavBarItem.route)
            return

        navigationManager.navigateTo(clickedItem, true)
    }





    fun isViewSelected(item: NavigationItem): Boolean {
        return navigationManager.selectedNavBarItem.route == item.route
    }


    /**
     * Navbar visibility status
     **/

    private val _showNavbar = mutableStateOf(false)
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
                showUserDetailsFetchFailAlertBox()
            }

            navigationManager.setRole(getLoggedInUser().role)
            navigationManager.selectView(getNavbarElements().first())
            navigationManager.navigateTo(getStartDestination(), true)
        }
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
    private val lock = ReentrantLock()


    override fun showLoadingScreen() {
        lock.lock()
        loadingScreenEnabled = true
    }


    override fun hideLoadingScreen() {
        loadingScreenEnabled = false
        lock.unlock()
    }

    override fun isLoadingScreenEnabled(): Boolean = loadingScreenEnabled


    /**
     * Logged In User Details Accessor
     **/


    override fun getLoggedInUser(): Employee {
        return loggedInUserDetailsViewModel.getUser()
    }

    override fun isAdmin(): Boolean {
        return loggedInUserDetailsViewModel.isAdmin()
    }


    /**
     * Other
     **/


}