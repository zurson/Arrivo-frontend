package com.thesis.arrivo.view_models.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.MainScaffoldViewModel

class MainScaffoldViewModelFactory(
    private val navigationManager: NavigationManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScaffoldViewModel::class.java)) {
            return MainScaffoldViewModel(
                navigationManager = navigationManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}