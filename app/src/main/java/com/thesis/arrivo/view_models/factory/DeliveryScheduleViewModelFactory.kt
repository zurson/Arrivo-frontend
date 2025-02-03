package com.thesis.arrivo.view_models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import com.thesis.arrivo.view_models.DeliveryScheduleViewModel
import com.thesis.arrivo.view_models.MapSharedViewModel


class DeliveryScheduleViewModelFactory(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor,
    private val mapSharedViewModel: MapSharedViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeliveryScheduleViewModel::class.java)) {
            return DeliveryScheduleViewModel(
                context = context,
                loadingScreenManager = loadingScreenManager,
                loggedInUserAccessor = loggedInUserAccessor,
                mapSharedViewModel = mapSharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}