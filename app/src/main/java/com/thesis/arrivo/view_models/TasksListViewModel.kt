package com.thesis.arrivo.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.thesis.arrivo.utilities.getCurrentDateMillis

class TasksListViewModel(
    navHostController: NavHostController
) : ViewModel() {

    /**
     * Date picker
     **/

    var selectedDate by mutableLongStateOf(getCurrentDateMillis())


    fun onDateSelected(dateMillis: Long?) {
        selectedDate = dateMillis ?: getCurrentDateMillis()
    }

}