package com.thesis.arrivo.ui.admin.admin_work_time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.work_time.WorkingHours
import com.thesis.arrivo.components.date_picker.DateRangePickerField
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.Settings.Companion.ANALYSIS_HOURS_UNIT
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.WorkTimeViewModel
import com.thesis.arrivo.view_models.factory.WorkTimeViewModelFactory

@Composable
fun WorkTimeView(workTimeViewModel: WorkTimeViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)
        val topGuideline = createGuidelineFromTop(Settings.START_END_PERCENTAGE)
        val bottomGuideline = createGuidelineFromBottom(Settings.START_END_PERCENTAGE)

        val contentRef = createRef()

        /* CONTENT */
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(contentRef) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(topGuideline)
                    bottom.linkTo(bottomGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            DateRangePicker(workTimeViewModel = workTimeViewModel)
            AnalysisSection(workTimeViewModel = workTimeViewModel, modifier = Modifier.weight(1f))
        }
    }
}


@Composable
private fun AnalysisSection(
    workTimeViewModel: WorkTimeViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AnalysisTitle()

        AnalysisData(
            workTimeViewModel = workTimeViewModel,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun AnalysisData(workTimeViewModel: WorkTimeViewModel, modifier: Modifier = Modifier) {
    val data = workTimeViewModel.workingHoursDataList

    if (data.isEmpty()) {
        EmptyList(loadingScreenStatusChecker = workTimeViewModel)
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        items(data) { item ->
            DataContainer(item)
        }
    }
}


@Composable
private fun DataContainer(
    workingHours: WorkingHours
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val firstName = capitalize(workingHours.firstName.lowercase())
        val lastName = capitalize(workingHours.lastName.lowercase())

        DialogRecord(
            label = "$firstName\n$lastName",
            value = "${workingHours.hours} $ANALYSIS_HOURS_UNIT"
        )
    }
}


@Composable
private fun AnalysisTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.working_time_data_section_title_text),
        color = MaterialTheme.colorScheme.primary,
        fontSize = dpToSp(R.dimen.working_time_data_section_title_text_size),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
    )
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