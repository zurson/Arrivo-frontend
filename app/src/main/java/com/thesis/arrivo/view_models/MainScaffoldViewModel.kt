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
import kotlinx.coroutines.launch

class MainScaffoldViewModel(private val navigationManager: NavigationManager) : ViewModel(),
    LoadingScreenManager {

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


    private var adminMode: Boolean = false


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
        NavigationItem.TasksListAdmin,
        NavigationItem.DeliveriesListAdmin,
        NavigationItem.EmployeesListAdmin,
    )

    fun getNavbarElements(): List<NavigationItem> =
        if (adminMode) navbarElementsAdmin else navbarElementUser


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


    fun getStartDestination(callback: (NavigationItem) -> Unit) {
        isUserAuthenticated { isAuthenticated ->
            if (isAuthenticated) {
                checkIsAdmin { isAdmin ->
                    adminMode = isAdmin
                    val startDestination = getNavbarElements().first()
                    callback(startDestination)
                }
            } else {
                callback(NavigationItem.Login)
            }
        }
    }

    private fun checkIsAdmin(callback: (Boolean) -> Unit) {
        val roleViewModel = RoleViewModel(this)
        viewModelScope.launch {
            roleViewModel.fetchUserRole()
            callback(roleViewModel.isAdmin())
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
        getStartDestination { dest -> navigationManager.navigateTo(dest, true) }
        setNavbarVisibility(true)
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
     * Other
     **/

}