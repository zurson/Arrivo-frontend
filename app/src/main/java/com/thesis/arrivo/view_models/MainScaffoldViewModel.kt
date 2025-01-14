package com.thesis.arrivo.view_models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.ui.admin.admin_tasks.create_or_edit_task.TaskToEdit
import com.thesis.arrivo.utilities.Location
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings.Companion.AUTH_ACCOUNT_STATUS_CHECK_INTERVAL_MS
import com.thesis.arrivo.utilities.changeActivity
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainScaffoldViewModel(
    private val context: Context,
    private var adminMode: Boolean,
    private val navigationManager: NavigationManager
) : ViewModel(), LoadingScreenManager {

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
        NavigationItem.EmployeesAdmin,
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
            val startDestination = if (!isAuthenticated) {
                NavigationItem.Login
            } else {
                getNavbarElements().first()
            }
            callback(startDestination)
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

    private var accountBlockJob: Job? = null


    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }


    private fun reset() {
        accountBlockJob?.cancel()

        changeActivity(
            context = context,
            destActivity = MainActivity::class,
            finish = true
        )
    }


    private fun listenAuthStatus() {
        if (accountBlockJob?.isActive == true)
            return

        accountBlockJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(AUTH_ACCOUNT_STATUS_CHECK_INTERVAL_MS)

                if (FirebaseAuth.getInstance().currentUser == null || !isConnectedToInternet())
                    continue

                isUserAuthenticated { isAuthenticated ->
                    if (!isAuthenticated)
                        reset()
                }
            }
        }
    }


    fun startAuthListeners() {
        listenAuthStatus()
    }


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