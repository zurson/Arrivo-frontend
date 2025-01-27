package com.thesis.arrivo.view_models

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import kotlinx.coroutines.launch

class RoadAccidentsAdminViewModel(
    context: Context,
    loadingScreenManager: LoadingScreenManager
) : RoadAccidentsViewModel(context, loadingScreenManager) {

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