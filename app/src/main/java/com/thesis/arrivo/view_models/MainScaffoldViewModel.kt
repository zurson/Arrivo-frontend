package com.thesis.arrivo.view_models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.thesis.arrivo.activities.MainActivity
import com.thesis.arrivo.components.NavigationItem
import com.thesis.arrivo.utilities.Settings.Companion.AUTH_ACCOUNT_STATUS_CHECK_INTERVAL_MS
import com.thesis.arrivo.utilities.changeActivity
import com.thesis.arrivo.utilities.navigateTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainScaffoldViewModel(
    val context: Context,
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
        getStartDestination { dest -> navigateTo(navController, dest) }
        setNavbarVisibility(true)
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