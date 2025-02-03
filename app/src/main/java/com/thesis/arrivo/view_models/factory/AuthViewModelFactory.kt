package com.thesis.arrivo.view_models.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.view_models.AuthViewModel
import com.thesis.arrivo.view_models.MainViewModel

class AuthViewModelFactory(
    private val mainViewModel: MainViewModel,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                mainViewModel = mainViewModel,
                loadingScreenManager = loadingScreenManager,
                loggedInUserAccessor = loggedInUserAccessor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}