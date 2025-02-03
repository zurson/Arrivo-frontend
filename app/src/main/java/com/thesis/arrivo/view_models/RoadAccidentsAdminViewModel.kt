package com.thesis.arrivo.view_models

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import kotlinx.coroutines.launch

class RoadAccidentsAdminViewModel(
    context: Context,
    loadingScreenManager: LoadingScreenManager,
    loggedInUserAccessor: LoggedInUserAccessor
) : RoadAccidentsViewModel(context, loadingScreenManager, loggedInUserAccessor) {

    init {
        fetchRoadAccidents()
    }

    override fun fetchRoadAccidents() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _allAccidents.clear()
                    _allAccidents.addAll(roadAccidentsRepository.getAllRoadAccidents())
                },
                onSuccess = { onFetchAccidentsSuccess() }
            )
        }
    }

}