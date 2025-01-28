package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.MainViewModel

class MainViewModelFactory(
    private val context: Context,
    private val navigationManager: NavigationManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                context = context,
                navigationManager = navigationManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}