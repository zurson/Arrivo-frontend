package com.thesis.arrivo.ui.admin.plan_a_day

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.thesis.arrivo.R
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.components.other_components.AppSpinner
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.view_models.PlanADayViewModel

@Composable
fun PlanADayView(loadingScreenManager: LoadingScreenManager, navigationManager: NavigationManager) {
    val context = LocalContext.current
    val planADayViewModel = remember {
        PlanADayViewModel(
            context = context,
            loadingScreenManager = loadingScreenManager,
            navigationManager = navigationManager
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* DATE AND FILTERS */
        val (dateAndFiltersRef) = createRefs()
        val dateAndFiltersTopGuideline = createGuidelineFromTop(0.05f)
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.25f)

        EmployeeSelectorAndDatePicker(
            planADayViewModel = planADayViewModel,
            modifier = Modifier.constrainAs(dateAndFiltersRef) {
                top.linkTo(dateAndFiltersTopGuideline)
                bottom.linkTo(dateAndFiltersBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* ROAD ACCIDENTS LIST */
        val (roadAccidentsListRef) = createRefs()
        val roadAccidentsListTopGuideline = createGuidelineFromTop(0.265f)

//        RoadAccidentsList(
//            roadAccidentsViewModel = roadAccidentsViewModel,
//            loadingScreenStatusChecker = loadingScreenManager,
//            modifier = Modifier.constrainAs(roadAccidentsListRef) {
//                top.linkTo(roadAccidentsListTopGuideline)
//                bottom.linkTo(parent.bottom)
//                start.linkTo(startGuideline)
//                end.linkTo(endGuideline)
//                width = Dimension.fillToConstraints
//                height = Dimension.fillToConstraints
//            })
    }
}


@Composable
private fun EmployeeSelectorAndDatePicker(
    modifier: Modifier = Modifier,
    planADayViewModel: PlanADayViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        AppSpinner(
            items = planADayViewModel.employeesList,
            label = stringResource(R.string.plan_a_day_employee_selector_label),
            selectedItem = planADayViewModel.selectedEmployee,
            onItemSelected = { planADayViewModel.onEmployeeSelected(it) },
            itemToString = { item -> planADayViewModel.employeeToString(item) },
            isError = planADayViewModel.employeeSpinnerError,
            errorMessage = stringResource(R.string.plan_a_day_employee_selector_error_message)
        )

        DatePickerField(
            selectedDate = planADayViewModel.getSelectedDate(),
            onDateSelected = { planADayViewModel.onDateSelected(it) },
        )
    }
}