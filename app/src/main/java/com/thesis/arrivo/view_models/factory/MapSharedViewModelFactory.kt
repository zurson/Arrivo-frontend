package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.MapSharedViewModel

class MapSharedViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapSharedViewModel::class.java)) {
            return MapSharedViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}