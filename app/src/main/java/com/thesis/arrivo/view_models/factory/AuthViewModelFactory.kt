package com.thesis.arrivo.view_models.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel

class AuthViewModelFactory(
    private val mainScaffoldViewModel: MainScaffoldViewModel,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                mainScaffoldViewModel = mainScaffoldViewModel,
                loadingScreenManager = loadingScreenManager,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}