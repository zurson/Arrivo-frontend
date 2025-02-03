package com.thesis.arrivo.ui.user.user_delivery_schedule_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.task.Task
import com.thesis.arrivo.communication.task.TaskStatus
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.modifiers.strikethroughX
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.ArrowRightIcon
import com.thesis.arrivo.components.other_components.ConfirmationDialog
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TaskAddress
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TaskDetailsDialog
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TaskTitle
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.DeliveryScheduleViewModel
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.MapSharedViewModel
import com.thesis.arrivo.view_models.factory.DeliveryScheduleViewModelFactory

@Composable
fun DeliveryScheduleView(deliveryScheduleViewModel: DeliveryScheduleViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShowTaskDetailsDialog(deliveryScheduleViewModel)
        ShowMarkAsFinishedConfirmationDialog(deliveryScheduleViewModel)
        ShowStartConfirmationDialog(deliveryScheduleViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* SCHEDULE SECTION */
        val (scheduleRef) = createRefs()
        val scheduleTopGuideline = createGuidelineFromTop(0.05f)
        val scheduleBottomGuideline = createGuidelineFromTop(0.8f)

        ScheduleSection(
            deliveryScheduleViewModel = deliveryScheduleViewModel,
            modifier = Modifier.constrainAs(scheduleRef) {
                top.linkTo(scheduleTopGuideline)
                bottom.linkTo(scheduleBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* START BUTTON */
        val (legendRef) = createRefs()
        val legendTopGuideline = createGuidelineFromTop(0.85f)

        BottomSector(
            deliveryScheduleViewModel = deliveryScheduleViewModel,
            modifier = Modifier.constrainAs(legendRef) {
                top.linkTo(legendTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}


@Composable
private fun BottomSector(
    modifier: Modifier = Modifier,
    deliveryScheduleViewModel: DeliveryScheduleViewModel
) {
    if (!deliveryScheduleViewModel.isStartButtonActive())
        return

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        AppButton(
            onClick = { deliveryScheduleViewModel.onStartButtonClick() },
            text = stringResource(R.string.delivery_schedule_start_button_text)
        )
    }
}


@Composable
private fun ScheduleSection(
    deliveryScheduleViewModel: DeliveryScheduleViewModel,
    modifier: Modifier = Modifier
) {
    val tasks = deliveryScheduleViewModel.delivery?.tasks

    if (tasks == null) {
        EmptyList(
            loadingScreenStatusChecker = deliveryScheduleViewModel,
            stringResourceId = R.string.delivery_schedule_no_content_message
        )
        return
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(tasks) { task ->
            TaskContainer(
                task = task,
                position = tasks.indexOf(task) + 1,
                active = deliveryScheduleViewModel.activeTask?.id == task.id,
                onClick = { deliveryScheduleViewModel.onTaskSelected(it) }
            )
        }
    }
}


@Composable
private fun TaskContainer(
    task: Task,
    position: Int,
    active: Boolean,
    onClick: (Task) -> Unit
) {
    val containerColor =
        if (active)
            Settings.getCurrentTaskContainerColor()
        else
            MaterialTheme.colorScheme.surfaceContainerHigh

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(containerColor)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick(task) }
            .padding(dimensionResource(R.dimen.accident_report_container_padding))
            .strikethroughX(task.status == TaskStatus.COMPLETED)
    ) {
        Text(
            text = "$position",
            fontSize = dpToSp(R.dimen.accident_report_container_position_text_size),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            TaskTitle(title = task.title)
            TaskAddress(address = task.addressText)
        }

        ArrowRightIcon(size = dimensionResource(R.dimen.accident_report_container_arrow_icon_size))
    }
}


@Composable
private fun ShowStartConfirmationDialog(deliveryScheduleViewModel: DeliveryScheduleViewModel) {
    if (!deliveryScheduleViewModel.showStartConfirmationDialog)
        return

    ConfirmationDialog(
        dialogTitle = stringResource(R.string.delivery_schedule_confirmation_title),
        lineOne = stringResource(R.string.delivery_schedule_confirmation_delivery_start_text),
        lineTwo = null,
        onYesClick = { deliveryScheduleViewModel.onStartConfirmationYesClick() },
        onNoClick = { deliveryScheduleViewModel.onStartConfirmationNoClick() },
        onDismiss = { deliveryScheduleViewModel.onStartConfirmationDismiss() },
    )
}


@Composable
private fun ShowMarkAsFinishedConfirmationDialog(deliveryScheduleViewModel: DeliveryScheduleViewModel) {
    if (!deliveryScheduleViewModel.showMarkAsFinishedConfirmationDialog)
        return

    ConfirmationDialog(
        dialogTitle = stringResource(R.string.delivery_schedule_confirmation_title),
        lineOne = stringResource(R.string.delivery_schedule_confirmation_mark_as_finished_text),
        lineTwo = null,
        onYesClick = { deliveryScheduleViewModel.onMarkAsFinishedConfirmationYesClick() },
        onNoClick = { deliveryScheduleViewModel.onMarkAsFinishedConfirmationNoClick() },
        onDismiss = { deliveryScheduleViewModel.onMarkAsFinishedConfirmationDismiss() },
    )
}


@Composable
private fun ShowTaskDetailsDialog(deliveryScheduleViewModel: DeliveryScheduleViewModel) {
    if (!deliveryScheduleViewModel.showTaskDetailsDialog)
        return

    val task = deliveryScheduleViewModel.selectedTask

    TaskDetailsDialog(
        task = task,
        onDismiss = { deliveryScheduleViewModel.onTaskDetailsDialogDismiss() },
        onButtonClick = { deliveryScheduleViewModel.onTaskDetailsDialogButtonClick() },
        buttonText = deliveryScheduleViewModel.detailsDialogButtonText()
    )

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm = MainViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val vm: DeliveryScheduleViewModel = viewModel(
        factory = DeliveryScheduleViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            loggedInUserAccessor = mainVm,
            mapSharedViewModel = MapSharedViewModel(
                LocalContext.current,
                loadingScreenManager = mainVm
            )
        )
    )

    Theme.ArrivoTheme {
        DeliveryScheduleView(vm)
    }
}