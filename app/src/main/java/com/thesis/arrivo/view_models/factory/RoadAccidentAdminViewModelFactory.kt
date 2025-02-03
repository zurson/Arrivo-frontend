package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.view_models.RoadAccidentsAdminViewModel

class RoadAccidentAdminViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoadAccidentsAdminViewModel::class.java)) {
            return RoadAccidentsAdminViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                loggedInUserAccessor = loggedInUserAccessor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}