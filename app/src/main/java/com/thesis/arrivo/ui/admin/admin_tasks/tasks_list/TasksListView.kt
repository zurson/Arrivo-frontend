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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.components.AppButton
import com.thesis.arrivo.components.ArrowRightIcon
import com.thesis.arrivo.components.Circle
import com.thesis.arrivo.components.LoadingScreen
import com.thesis.arrivo.components.bounceClick
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.Location
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.view_models.TasksListViewModel
import java.time.LocalDateTime

@Composable
fun TasksListView(navHostController: NavHostController) {
    val context = LocalContext.current
    val tasksListViewModel = remember { TasksListViewModel(navHostController, context) }

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

        LoadingScreen(enabled = tasksListViewModel.tasksFetchingInProgress)
    }
}


@Composable
private fun TasksList(
    modifier: Modifier = Modifier,
    tasksListViewModel: TasksListViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(tasksListViewModel.tasksToShow) { task ->
                TaskContainer(task = task)
            }
        }
    }
}


@Composable
private fun TaskContainer(
    task: Task
) {
    when (task.status) {
        TaskStatus.COMPLETED -> TaskCompletedOrFreeContainer(task = task)
        TaskStatus.UNASSIGNED -> TaskCompletedOrFreeContainer(task = task)
        TaskStatus.IN_PROGRESS -> TaskAssignedContainer(task = task)
    }
}


@Composable
private fun TaskCompletedOrFreeContainer(
    task: Task
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Circle(
            size = 40.dp,
            color = TasksListViewModel.getFilterColor(task.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            TaskTitle(title = task.title)
            TaskAddress(address = task.addressText)
        }

        ArrowRightIcon(size = 50.dp)
    }
}


@Composable
private fun TaskAssignedContainer(
    task: Task
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Circle(
            size = 40.dp,
            color = TasksListViewModel.getFilterColor(task.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

        ArrowRightIcon(size = 50.dp)
    }
}


@Composable
fun TaskTitle(
    title: String
) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 20.sp,
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
fun TaskAddress(
    address: String
) {
    Text(
        text = address,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 16.sp,
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
fun TaskEmployeeData(
    firstName: String?,
    lastName: String?
) {
    val finalFirstName = firstName ?: "Error"
    val finalLastName = lastName ?: "Error"

    Text(
        text = "$finalFirstName $finalLastName",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 24.sp,
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
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(64.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Legend()

            AppButton(
                onClick = { tasksListViewModel.onAddTaskButtonClick() },
                text = "Add task",
                icon = Icons.Filled.Add,
            )
        }
    }
}


@Composable
private fun Legend() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Circle(
            size = 22.dp,
            color = TasksListViewModel.getFilterColor(filter)
        )

        Text(
            text = TasksListViewModel.getRenamedFilter(filter),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp
        )
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
            selectedDate = tasksListViewModel.getSelectedDate(),
            onDateSelected = { tasksListViewModel.onDateSelected(it) }
        )

        FiltersList(tasksListViewModel = tasksListViewModel)
    }
}


@Composable
private fun FiltersList(
    tasksListViewModel: TasksListViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
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
    val color = if (active) Settings.TASK_FREE_COLOR else MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick()
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .clickable { tasksListViewModel.toggleFilterActive(filter) }
    ) {
        Text(
            text = capitalize(TasksListViewModel.getRenamedFilter(filter).lowercase()),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}


@Preview
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
//        TasksListView(rememberNavController())
        TaskContainer(
            task = Task(
                status = TaskStatus.UNASSIGNED,
                id = 1,
                title = "Test title",
                location = Location(longitude = 52.1231, latitude = 22.3212),
                addressText = "Opoczno Spacerowa 1",
                assignedDate = null,
                employee = null,
            )
        )
    }
}