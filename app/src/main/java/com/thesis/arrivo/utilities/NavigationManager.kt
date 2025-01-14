package com.thesis.arrivo.utilities

import androidx.navigation.NavHostController
import com.thesis.arrivo.components.navigation.NavigationItem

class NavigationManager(private val navController: NavHostController) {

    fun navigateTo(navigationItem: NavigationItem, clearHistory: Boolean = false) {
        runOnMainThread {
            navController.navigate(navigationItem.route) {
                if (clearHistory) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}
