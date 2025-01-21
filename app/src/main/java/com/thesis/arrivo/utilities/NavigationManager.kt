package com.thesis.arrivo.utilities

import androidx.navigation.NavHostController
import com.thesis.arrivo.components.navigation.NavigationItem

class NavigationManager(private val navController: NavHostController) {

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
        }
    }


    fun navigateBack() {
        navController.popBackStack()
    }
}
