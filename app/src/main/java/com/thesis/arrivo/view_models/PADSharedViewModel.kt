package com.thesis.arrivo.view_models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.thesis.arrivo.communication.task.Task

class PADSharedViewModel : ViewModel() {

    private val _selectedTasks = mutableStateListOf<Task>()
    var selectedTasks: List<Task>
        get() = _selectedTasks
        set(value) {
            _selectedTasks.clear()
            _selectedTasks.addAll(value)
        }
}