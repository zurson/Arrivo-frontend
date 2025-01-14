package com.thesis.arrivo.communication

import android.content.Context
import com.thesis.arrivo.R
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.mapError
import com.thesis.arrivo.utilities.showErrorDialog

class ServerRequestManager(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) {

    suspend fun sendRequest(
        actionToPerform: suspend () -> Unit,
        onFailure: (Exception) -> Unit = { },
        showOnFailureDefaultInfoBox: Boolean = true
    ) {
        try {
            loadingScreenManager.showLoadingScreen()
            actionToPerform()
        } catch (e: Exception) {
            if (showOnFailureDefaultInfoBox) onFailureToast(e)
            onFailure(e)
        } finally {
            loadingScreenManager.hideLoadingScreen()
        }
    }


    private fun onFailureToast(exception: Exception) {
        showErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            errorResponse = mapError(exception, context)
        )
    }

}