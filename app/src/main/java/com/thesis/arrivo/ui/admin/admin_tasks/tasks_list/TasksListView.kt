package com.thesis.arrivo.ui.admin.admin_tasks.tasks_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.ArrowRightIcon
import com.thesis.arrivo.components.other_components.Circle
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.TasksListViewModel

@Composable
fun TasksListView(
    mainScaffoldViewModel: MainScaffoldViewModel,
    loadingScreenManager: LoadingScreenManager,
    navigationManager: NavigationManager
) {
    val context = LocalContext.current
    val tasksListViewModel =
        remember {
            TasksListViewModel(
                context = context,
                mainScaffoldViewModel = mainScaffoldViewModel,
                loadingScreenManager = loadingScreenManager,
                navigationManager = navigationManager
            )
        }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShowTaskDetailsDialog(tasksListViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* DATE AND FILTERS */
        val (dateAndFiltersRef) = createRefs()
        val dateAndFiltersTopGuideline = createGuidelineFromTop(0.05f)
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.25f)

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

        /* TASKS LIST */
        val (tasksListRef) = createRefs()
        val tasksListTopGuideline = createGuidelineFromTop(0.265f)
        val tasksListBottomGuideline = createGuidelineFromTop(0.8f)

        TasksList(
            tasksListViewModel = tasksListViewModel,
            loadingScreenStatusChecker = mainScaffoldViewModel,
            modifier = Modifier.constrainAs(tasksListRef) {
                top.linkTo(tasksListTopGuideline)
                bottom.linkTo(tasksListBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* BOTTOM SECTOR */
        val (bottomSectorListRef) = createRefs()
        val bottomSectorTopGuideline = createGuidelineFromTop(0.82f)

        BottomSector(
            tasksListViewModel = tasksListViewModel,
            modifier = Modifier.constrainAs(bottomSectorListRef) {
                top.linkTo(bottomSectorTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}

@Composable
private fun ShowTaskDetailsDialog(tasksListViewModel: TasksListViewModel) {
    if (tasksListViewModel.showTaskDetailsDialog) {
        TaskDetailsDialog(
            task = tasksListViewModel.selectedTask,
            onDismiss = { tasksListViewModel.onTaskDismiss() },
            onEditButtonClick = { tasksListViewModel.onTaskEditButtonClick() }
        )
    }
}


@Composable
private fun TasksList(
    modifier: Modifier = Modifier,
    tasksListViewModel: TasksListViewModel,
    loadingScreenStatusChecker: LoadingScreenStatusChecker
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (tasksListViewModel.tasksToShow.isEmpty()) {
            EmptyList(loadingScreenStatusChecker)
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            items(tasksListViewModel.tasksToShow) { task ->
                TaskContainer(task = task, tasksListViewModel = tasksListViewModel)
            }
        }
    }
}


@Composable
private fun TaskContainer(
    tasksListViewModel: TasksListViewModel,
    task: Task
) {
    when (task.status) {
        TaskStatus.COMPLETED -> TaskCompletedOrFreeContainer(
            task = task,
            tasksListViewModel = tasksListViewModel
        )

        TaskStatus.UNASSIGNED -> TaskCompletedOrFreeContainer(
            task = task,
            tasksListViewModel = tasksListViewModel
        )

        TaskStatus.IN_PROGRESS -> TaskAssignedContainer(
            task = task,
            tasksListViewModel = tasksListViewModel
        )
    }
}


@Composable
private fun TaskCompletedOrFreeContainer(
    tasksListViewModel: TasksListViewModel,
    task: Task
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { tasksListViewModel.onTaskSelected(task) }
            .padding(dimensionResource(R.dimen.tasks_list_container_padding))
    ) {
        Circle(
            size = dimensionResource(R.dimen.tasks_list_task_circle_icon_size),
            color = TasksListViewModel.getFilterColor(task.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            TaskTitle(title = task.title)
            TaskAddress(address = task.addressText)
        }

        ArrowRightIcon(size = dimensionResource(R.dimen.tasks_list_arrow_right_icon_size))
    }
}


@Composable
private fun TaskAssignedContainer(
    tasksListViewModel: TasksListViewModel,
    task: Task
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { tasksListViewModel.onTaskSelected(task) }
            .padding(dimensionResource(R.dimen.tasks_list_container_padding))
    ) {
        Circle(
            size = dimensionResource(R.dimen.tasks_list_task_circle_icon_size),
            color = TasksListViewModel.getFilterColor(task.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            TaskTitle(title = task.title)
            TaskEmployeeData(
                firstName = task.employee?.firstName,
                lastName = task.employee?.lastName
            )
            TaskAddress(address = task.addressText)
        }

        ArrowRightIcon(size = dimensionResource(R.dimen.tasks_list_arrow_right_icon_size))
    }
}


@Composable
private fun TaskTitle(
    title: String
) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = dpToSp(R.dimen.tasks_list_task_title_text_size),
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun TaskAddress(
    address: String
) {
    Text(
        text = address,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        fontSize = dpToSp(R.dimen.tasks_list_task_location_text_size),
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun TaskEmployeeData(
    firstName: String?,
    lastName: String?
) {
    val finalFirstName = firstName ?: stringResource(R.string.tasks_list_emp_name_error)
    val finalLastName = lastName ?: stringResource(R.string.tasks_list_emp_name_error)

    Text(
        text = "$finalFirstName $finalLastName",
        color = MaterialTheme.colorScheme.primary,
        fontSize = dpToSp(R.dimen.tasks_list_task_employee_text_size),
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun BottomSector(
    modifier: Modifier = Modifier,
    tasksListViewModel: TasksListViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.tasks_list_bottom_sector_divider_height))
                .background(MaterialTheme.colorScheme.onBackground)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.tasks_list_bottom_sector_horizontal_space)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Legend()

            AppButton(
                onClick = { tasksListViewModel.onAddTaskButtonClick() },
                text = stringResource(R.string.tasks_list_add_tasks_button_text),
                icon = Icons.Filled.Add,
            )
        }
    }
}


@Composable
private fun Legend() {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.tasks_list_legend_vertical_space))
    ) {
        LegendItem(filter = TaskStatus.COMPLETED)
        LegendItem(filter = TaskStatus.UNASSIGNED)
        LegendItem(filter = TaskStatus.IN_PROGRESS)
    }
}


@Composable
private fun LegendItem(
    filter: TaskStatus,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.tasks_list_legend_horizontal_space)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Circle(
            size = dimensionResource(R.dimen.tasks_list_legend_circle_size),
            color = TasksListViewModel.getFilterColor(filter)
        )

        Text(
            text = TasksListViewModel.getRenamedFilter(filter),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.tasks_list_legend_text_size)
        )
    }
}


@Composable
private fun DatePickerAndFilters(
    modifier: Modifier = Modifier,
    tasksListViewModel: TasksListViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        DatePickerField(
            selectedDate = tasksListViewModel.getSelectedDate(),
            onDateSelected = { tasksListViewModel.onDateSelected(it) },
        )

        FiltersList(
            tasksListViewModel = tasksListViewModel,
        )
    }
}


@Composable
private fun FiltersList(
    tasksListViewModel: TasksListViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Filter(
            filter = TaskStatus.UNASSIGNED,
            modifier = Modifier.weight(1f),
            tasksListViewModel = tasksListViewModel
        )

        Filter(
            filter = TaskStatus.IN_PROGRESS,
            modifier = Modifier.weight(1f),
            tasksListViewModel = tasksListViewModel
        )

        Filter(
            filter = TaskStatus.COMPLETED,
            modifier = Modifier.weight(1f),
            tasksListViewModel = tasksListViewModel
        )
    }
}


@Composable
private fun Filter(
    modifier: Modifier = Modifier,
    filter: TaskStatus,
    tasksListViewModel: TasksListViewModel
) {
    val active = tasksListViewModel.getActiveFilters().contains(filter)
    val color =
        if (active) Settings.FILTER_ACTIVE_COLOR else MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(color)
            .clickable { tasksListViewModel.toggleFilterActive(filter) }
    ) {
        Text(
            text = capitalize(TasksListViewModel.getRenamedFilter(filter).lowercase()),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dpToSp(R.dimen.tasks_list_filter_text_size),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.tasks_list_filter_padding))
        )
    }
}


@Preview
@Composable
private fun Preview() {
//    Theme.ArrivoTheme {
//        TasksListView(MainScaffoldViewModel())
//        TaskContainer(
//            task = Task(
//                status = TaskStatus.UNASSIGNED,
//                id = 1,
//                title = "Test title",
//                location = Location(longitude = 52.1231, latitude = 22.3212),
//                addressText = "Opoczno Spacerowa 1",
//                assignedDate = null,
//                employee = null,
//            )
//        )
//    }
}