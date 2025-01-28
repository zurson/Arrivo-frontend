package com.thesis.arrivo.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager

class DeliveryScheduleViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModel() {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)



}