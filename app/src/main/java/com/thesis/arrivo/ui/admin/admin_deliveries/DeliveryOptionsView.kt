package com.thesis.arrivo.ui.admin.admin_deliveries

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppCheckbox
import com.thesis.arrivo.components.other_components.AppSpinner
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TaskDetailsDialog
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.DeliveryOptionsViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.DeliveryOptionsViewModelFactory

@Composable
fun DeliveryTasksView(deliveryOptionsViewModel: DeliveryOptionsViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShowTaskDetailsDialog(deliveryOptionsViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* DATE AND FILTERS */
        val (dateAndFiltersRef) = createRefs()
        val dateAndFiltersTopGuideline = createGuidelineFromTop(0.05f)
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.3f)

        EmployeeSelectorAndDatePicker(
            deliveryOptionsViewModel = deliveryOptionsViewModel,
            modifier = Modifier.constrainAs(dateAndFiltersRef) {
                top.linkTo(dateAndFiltersTopGuideline)
                bottom.linkTo(dateAndFiltersBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /* ROAD ACCIDENTS LIST */
        val (availableTasksListRef) = createRefs()
        val availableTasksListTopGuideline = createGuidelineFromTop(0.31f)
        val availableTasksListBottomGuideline = createGuidelineFromTop(0.87f)

        AvailableTasksList(
            deliveryOptionsViewModel = deliveryOptionsViewModel,
            modifier = Modifier.constrainAs(availableTasksListRef) {
                top.linkTo(availableTasksListTopGuideline)
                bottom.linkTo(availableTasksListBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* BUTTON - NEXT */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.88f)

        ButtonSection(
            deliveryOptionsViewModel = deliveryOptionsViewModel,
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(buttonTopGuideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )
    }
}


@Composable
private fun EmployeeSelectorAndDatePicker(
    modifier: Modifier = Modifier,
    deliveryOptionsViewModel: DeliveryOptionsViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        AppSpinner(
            items = deliveryOptionsViewModel.employeesList,
            label = stringResource(R.string.delivery_employee_selector_label),
            selectedItem = deliveryOptionsViewModel.selectedEmployee,
            onItemSelected = { deliveryOptionsViewModel.onEmployeeSelected(it) },
            itemToString = { item -> deliveryOptionsViewModel.employeeToString(item) },
            isError = deliveryOptionsViewModel.employeeSpinnerError,
            errorMessage = stringResource(R.string.delivery_employee_selector_error_message),
        )

        DatePickerField(
            selectedDate = deliveryOptionsViewModel.getSelectedDate(),
            onDateSelected = { deliveryOptionsViewModel.onDateSelected(it) },
            isError = deliveryOptionsViewModel.isDatePickerError,
            errorMessage = stringResource(R.string.delivery_tasks_date_picker_error_message)
        )
    }
}


@Composable
fun AvailableTasksList(
    modifier: Modifier = Modifier,
    deliveryOptionsViewModel: DeliveryOptionsViewModel,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.delivery_available_tasks_section_title),
            fontSize = dpToSp(R.dimen.delivery_available_tasks_sector_title_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (deliveryOptionsViewModel.availableTasks.isEmpty()) {
            EmptyList(
                loadingScreenStatusChecker = deliveryOptionsViewModel,
                modifier = Modifier.weight(1f)
            )
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            items(deliveryOptionsViewModel.availableTasks, key = { it.id }) { task ->
                AvailableTaskContainer(
                    task = task,
                    deliveryOptionsViewModel = deliveryOptionsViewModel,
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
                )
            }
        }
    }
}


@Composable
fun AvailableTaskContainer(
    task: Task,
    deliveryOptionsViewModel: DeliveryOptionsViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = modifier
            .bounceClick()
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(dimensionResource(R.dimen.delivery_available_tasks_container_padding))
            .clickable { deliveryOptionsViewModel.onTaskSelected(task) }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.delivery_available_tasks_container_content_vertical_space)),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.title,
                fontSize = dpToSp(R.dimen.delivery_tasks_container_title_text_size),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = task.addressText,
                fontSize = dpToSp(R.dimen.delivery_tasks_container_address_text_size),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        AppCheckbox(
            checked = deliveryOptionsViewModel.isTaskChecked(task),
            onCheckedChange = { deliveryOptionsViewModel.onTaskCheckedChange(task) },
            size = dimensionResource(R.dimen.delivery_tasks_container_checkbox_size),
            modifier = Modifier.padding(end = dimensionResource(R.dimen.delivery_tasks_container_checkbox_end_padding))
        )
    }
}


@Composable
private fun ButtonSection(
    modifier: Modifier = Modifier,
    deliveryOptionsViewModel: DeliveryOptionsViewModel
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(),
    ) {
        AppButton(
            onClick = { deliveryOptionsViewModel.onButtonNextClick() },
            text = stringResource(R.string.delivery_button_next_text),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        )
    }
}


@Composable
private fun ShowTaskDetailsDialog(deliveryOptionsViewModel: DeliveryOptionsViewModel) {
    if (!deliveryOptionsViewModel.showTaskDetailsDialog)
        return

    TaskDetailsDialog(
        task = deliveryOptionsViewModel.selectedTask,
        onDismiss = { deliveryOptionsViewModel.onTaskDialogDismiss() },
        onButtonClick = { deliveryOptionsViewModel.onTaskDialogDismiss() },
        buttonText = stringResource(R.string.delivery_task_details_dialog_dismiss_button_text),
    )
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainScaffoldViewModel(
        navigationManager = NavigationManager(rememberNavController()),
        context = LocalContext.current,
        adminMode = true
    )

    val deliveryOptionsViewModel: DeliveryOptionsViewModel = viewModel(
        factory = DeliveryOptionsViewModelFactory(
            editMode = false, 
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            navigationManager = NavigationManager(rememberNavController()),
            deliverySharedViewModel = DeliverySharedViewModel()
        )
    )

    Theme.ArrivoTheme {
        DeliveryTasksView(deliveryOptionsViewModel)
    }
}