package com.thesis.arrivo.view_models

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoggedInUserAccessor
import kotlinx.coroutines.launch

class RoadAccidentsUserViewModel(
    context: Context,
    loadingScreenManager: LoadingScreenManager,
    private val loggedInUserAccessor: LoggedInUserAccessor
) : RoadAccidentsViewModel(context, loadingScreenManager, loggedInUserAccessor) {

    init {
        showMarkAsResolvedButton = false
        fetchRoadAccidents()
    }

    override fun fetchRoadAccidents() {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _allAccidents.clear()
                    _allAccidents.addAll(
                        roadAccidentsRepository.getAllRoadAccidents(
                            loggedInUserAccessor.getLoggedInUser().id
                        )
                    )
                },
                onSuccess = { onFetchAccidentsSuccess() }
            )
        }
    }


}