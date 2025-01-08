package com.thesis.arrivo.ui.admin.admin_tasks.tasks_list

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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.view_models.TasksListViewModel

@Composable
fun TasksListView(navHostController: NavHostController) {
    val context = LocalContext.current
    val tasksListViewModel = remember { TasksListViewModel(navHostController) }

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
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.3f)

        DatePickerAndFilters(
            tasksListViewModel = tasksListViewModel,
            modifier = Modifier.constrainAs(dateAndFiltersRef) {
                top.linkTo(dateAndFiltersTopGuideline)
                bottom.linkTo(dateAndFiltersBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* CREATE TASK BUTTON */
//        val (buttonRef) = createRefs()
//        val buttonTopGuideline = createGuidelineFromTop(0.89f)
//        val buttonBottomGuideline = createGuidelineFromTop(0.96f)
//
//        TaskCreateButton(newTaskViewModel = newTaskViewModel,
//            context = context,
//            modifier = Modifier.constrainAs(buttonRef) {
//                top.linkTo(buttonTopGuideline)
//                bottom.linkTo(buttonBottomGuideline)
//                start.linkTo(startGuideline)
//                end.linkTo(endGuideline)
//                width = Dimension.fillToConstraints
//                height = Dimension.fillToConstraints
//            })

    }
}


@Composable
private fun DatePickerAndFilters(
    modifier: Modifier = Modifier,
    tasksListViewModel: TasksListViewModel
) {
    /**
     * Title
     **/
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        DatePickerField(
            selectedDate = tasksListViewModel.selectedDate,
            onDateSelected = { tasksListViewModel.onDateSelected(it) }
        )
    }
}