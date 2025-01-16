package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.DeliveriesListViewModel


class DeliveriesListViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeliveriesListViewModel::class.java)) {
            return DeliveriesListViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                navigationManager = navigationManager,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}