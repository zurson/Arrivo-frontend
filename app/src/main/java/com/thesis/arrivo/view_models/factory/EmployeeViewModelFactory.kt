package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.EmployeeViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel


class EmployeeViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
    private val mainScaffoldViewModel: MainScaffoldViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeViewModel::class.java)) {
            return EmployeeViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                navigationManager = navigationManager,
                mainScaffoldViewModel = mainScaffoldViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}