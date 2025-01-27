package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.view_models.AccidentReportViewModel


class AccidentReportViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor,
    private val navigationManager: NavigationManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccidentReportViewModel::class.java)) {
            return AccidentReportViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                loggedInUserAccessor = loggedInUserAccessor,
                navigationManager = navigationManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}