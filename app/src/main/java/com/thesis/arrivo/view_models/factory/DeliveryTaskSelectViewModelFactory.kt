package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.DeliveryTaskSelectViewModel

class DeliveryTaskSelectViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
    private val deliverySharedViewModel: DeliverySharedViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeliveryTaskSelectViewModel::class.java)) {
            return DeliveryTaskSelectViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                navigationManager = navigationManager,
                deliverySharedViewModel = deliverySharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}