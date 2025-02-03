package com.thesis.arrivo.ui.admin.admin_work_time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.date_picker.DateRangePickerField
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.WorkTimeViewModel
import com.thesis.arrivo.view_models.factory.WorkTimeViewModelFactory

@Composable
fun WorkTimeView(workTimeViewModel: WorkTimeViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        DateRangePicker(workTimeViewModel = workTimeViewModel)
    }
}


@Composable
private fun DateRangePicker(
    workTimeViewModel: WorkTimeViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        DateRangePickerField(
            selectedDates = workTimeViewModel.selectedDates,
            onDateRangeSelected = { workTimeViewModel.onDateRangeSelected(it) },
        )
    }
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val vm: WorkTimeViewModel = viewModel(
        factory = WorkTimeViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm
        )
    )

    Theme.ArrivoTheme {
        WorkTimeView(vm)
    }
}