package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_ASSIGNED_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_COMPLETED_COLOR
import com.thesis.arrivo.utilities.Settings.Companion.DELIVERY_IN_PROGRESS_COLOR
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager

class DeliveriesListViewModel(
    private val context: Context,
    private val navigationManager: NavigationManager,
    private val loadingScreenManager: LoadingScreenManager,
) : ViewModel() {

    companion object {
        fun getRenamedFilter(filer: DeliveryStatus): String {
            return when (filer) {
                DeliveryStatus.COMPLETED -> "Completed"
                DeliveryStatus.IN_PROGRESS -> "In Delivery"
                DeliveryStatus.ASSIGNED -> "Assigned"
            }
        }


        fun getFilterColor(filer: DeliveryStatus): Color {
            return when (filer) {
                DeliveryStatus.COMPLETED -> DELIVERY_COMPLETED_COLOR
                DeliveryStatus.IN_PROGRESS -> DELIVERY_IN_PROGRESS_COLOR
                DeliveryStatus.ASSIGNED -> DELIVERY_ASSIGNED_COLOR
            }
        }
    }


    /**
     * Create Delivery Button
     **/


    fun onCreateDeliveryButtonClick() {
        navigationManager.navigateTo(NavigationItem.DeliveryTasksAdmin)
    }


}