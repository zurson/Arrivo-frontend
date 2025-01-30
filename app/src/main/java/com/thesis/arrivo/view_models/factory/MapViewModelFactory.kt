package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.MapSharedViewModel
import com.thesis.arrivo.view_models.MapViewModel

class MapViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                mapSharedViewModel = mapSharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}