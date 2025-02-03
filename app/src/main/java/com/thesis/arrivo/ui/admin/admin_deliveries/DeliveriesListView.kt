package com.thesis.arrivo.ui.admin.admin_deliveries

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.thesis.arrivo.communication.delivery.Delivery
import com.thesis.arrivo.communication.delivery.DeliveryStatus
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.date_picker.DefaultDatePickerField
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppFilter
import com.thesis.arrivo.components.other_components.AppHorizontalDivider
import com.thesis.arrivo.components.other_components.AppLegendItem
import com.thesis.arrivo.components.other_components.ArrowRightIcon
import com.thesis.arrivo.components.other_components.Circle
import com.thesis.arrivo.components.other_components.ConfirmationDialog
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.admin.admin_tasks.tasks_list.TaskDetailsDialog
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.DeliveriesListViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.factory.DeliveriesListViewModelFactory

@Composable
fun DeliveriesListView(deliveriesListViewModel: DeliveriesListViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShowDeliveryDetailsDialog(deliveriesListViewModel)
        ShowDeliveryTaskDetailsDialog(deliveriesListViewModel)
        ShowDeliveryCancelConfirmationDialog(deliveriesListViewModel)
        ShowTrackDialog(deliveriesListViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* DATE AND FILTERS */
        val (dateAndFiltersRef) = createRefs()
        val dateAndFiltersTopGuideline = createGuidelineFromTop(0.05f)
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.25f)

        DatePickerAndFilters(
            deliveriesListViewModel = deliveriesListViewModel,
            modifier = Modifier.constrainAs(dateAndFiltersRef) {
                top.linkTo(dateAndFiltersTopGuideline)
                bottom.linkTo(dateAndFiltersBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* DELIVERIES LIST */
        val (deliveriesListRef) = createRefs()
        val deliveriesListTopGuideline = createGuidelineFromTop(0.265f)
        val deliveriesListBottomGuideline = createGuidelineFromTop(0.84f)

        DeliveriesList(
            deliveriesListViewModel = deliveriesListViewModel,
            modifier = Modifier.constrainAs(deliveriesListRef) {
                top.linkTo(deliveriesListTopGuideline)
                bottom.linkTo(deliveriesListBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* BOTTOM SECTOR */
        val (bottomSectorListRef) = createRefs()
        val bottomSectorTopGuideline = createGuidelineFromTop(0.85f)

        BottomSector(
            deliveriesListViewModel = deliveriesListViewModel,
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
private fun DeliveriesList(
    modifier: Modifier = Modifier,
    deliveriesListViewModel: DeliveriesListViewModel,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (deliveriesListViewModel.deliveriesToShow.isEmpty()) {
            EmptyList(deliveriesListViewModel)
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            items(deliveriesListViewModel.deliveriesToShow) { delivery ->
                DeliveryContainer(
                    delivery = delivery,
                    deliveriesListViewModel = deliveriesListViewModel
                )
            }
        }
    }
}


@Composable
private fun DeliveryContainer(
    delivery: Delivery,
    deliveriesListViewModel: DeliveriesListViewModel
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
            .clickable { deliveriesListViewModel.onDeliverySelected(delivery) }
            .padding(dimensionResource(R.dimen.delivery_list_container_padding))
    ) {
        Circle(
            size = dimensionResource(R.dimen.delivery_list_container_circle_icon_size),
            color = DeliveriesListViewModel.getFilterColor(delivery.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            DeliveryEmployeeData(
                firstName = delivery.employee.firstName,
                lastName = delivery.employee.lastName
            )
            DeliveryStartDate(date = delivery.assignedDate.toString())
        }

        ArrowRightIcon(size = dimensionResource(R.dimen.delivery_list_container_arrow_icon_size))
    }
}


@Composable
private fun DeliveryEmployeeData(
    firstName: String?,
    lastName: String?
) {
    val finalFirstName = firstName ?: stringResource(R.string.emp_name_error)
    val finalLastName = lastName ?: stringResource(R.string.emp_name_error)

    Text(
        text = "$finalFirstName $finalLastName",
        color = MaterialTheme.colorScheme.primary,
        fontSize = dpToSp(R.dimen.delivery_list_container_employee_text_size),
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun DeliveryStartDate(date: String) {
    Text(
        text = date,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = dpToSp(R.dimen.delivery_list_container_date_text_size),
        maxLines = 1,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun DatePickerAndFilters(
    modifier: Modifier = Modifier,
    deliveriesListViewModel: DeliveriesListViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        DefaultDatePickerField(
            selectedDate = deliveriesListViewModel.getSelectedDate(),
            onDateSelected = { deliveriesListViewModel.onDateSelected(it) },
        )

        FiltersList(deliveriesListViewModel = deliveriesListViewModel)
    }
}


@Composable
private fun FiltersList(
    deliveriesListViewModel: DeliveriesListViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        DeliveryStatus.entries.toTypedArray().forEach { filter ->
            AppFilter(
                filter = filter,
                modifier = Modifier.weight(1f),
                isActive = deliveriesListViewModel.getActiveFilters().contains(filter),
                filterToString = { DeliveriesListViewModel.getRenamedFilter(filter) },
                onSelected = { deliveriesListViewModel.toggleFilterActive(it) }
            )
        }
    }
}


@Composable
private fun BottomSector(
    modifier: Modifier = Modifier,
    deliveriesListViewModel: DeliveriesListViewModel
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        AppHorizontalDivider(height = dimensionResource(R.dimen.delivery_list_bottom_sector_divider_height))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.delivery_list_bottom_sector_horizontal_space)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Legend()

            AppButton(
                onClick = { deliveriesListViewModel.onCreateDeliveryButtonClick() },
                text = stringResource(R.string.delivery_list_create_delivery_button_text),
                icon = Icons.Filled.Add,
            )
        }
    }
}


@Composable
private fun Legend() {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        DeliveryStatus.entries.forEach { deliveryStatus ->
            AppLegendItem(
                filterName = DeliveriesListViewModel.getRenamedFilter(deliveryStatus),
                circleColor = DeliveriesListViewModel.getFilterColor(deliveryStatus)
            )
        }
    }
}


@Composable
private fun ShowDeliveryDetailsDialog(deliveryListViewModel: DeliveriesListViewModel) {
    if (!deliveryListViewModel.showDeliveryDetails)
        return

    DeliveryDetailsDialog(
        delivery = deliveryListViewModel.selectedDelivery,
        onDismiss = { deliveryListViewModel.onDeliveryDetailsDismiss() },
        onEditButtonClick = { deliveryListViewModel.onDeliveryDetailsEditButtonClick() },
        onDeliveryCancelButtonClick = { deliveryListViewModel.onDeliveryDetailsCancelButtonClick() },
        onTaskSelected = { deliveryListViewModel.onDeliveryDetailsTaskSelected(it) },
        showEditButton = { deliveryListViewModel.showDeliveryDetailsEditButton(it) },
        showCancelButton = { deliveryListViewModel.showDeliveryDetailsCancelButton(it) },
        onTrackButtonClick = { deliveryListViewModel.onTrackButtonClick() }
    )
}


@Composable
private fun ShowDeliveryTaskDetailsDialog(deliveryListViewModel: DeliveriesListViewModel) {
    if (!deliveryListViewModel.showTaskDetails)
        return

    TaskDetailsDialog(
        task = deliveryListViewModel.selectedTask,
        onDismiss = { deliveryListViewModel.onDeliveryDetailsTaskDialogDismiss() },
        onButtonClick = { deliveryListViewModel.onDeliveryDetailsTaskDialogButtonClick() },
        buttonText = stringResource(R.string.delivery_details_dialog_task_dismiss_button_text)
    )
}


@Composable
private fun ShowDeliveryCancelConfirmationDialog(deliveriesListViewModel: DeliveriesListViewModel) {
    if (!deliveriesListViewModel.showDeliveryCancelConfirmationDialog)
        return

    ConfirmationDialog(
        dialogTitle = stringResource(R.string.delivery_details_dialog__delivery_cancel_confirmation_dialog_title),
        lineOne = stringResource(R.string.delivery_details_dialog__delivery_confirmation_dialog_text),
        lineTwo = null,
        onYesClick = { deliveriesListViewModel.onDeliveryCancelConfirmationYesClick() },
        onNoClick = { deliveriesListViewModel.onDeliveryCancelConfirmationNoClick() },
        onDismiss = { deliveriesListViewModel.onDeliveryCancelConfirmationDismiss() },
    )
}


@Composable
private fun ShowTrackDialog(deliveryListViewModel: DeliveriesListViewModel) {
    if (!deliveryListViewModel.showTrackDialog)
        return

    DeliveryTrackDialog(
        onDismiss = { deliveryListViewModel.onTrackDialogDismiss() },
        waypoints = deliveryListViewModel.waypoints,
    )
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val deliveriesListView: DeliveriesListViewModel = viewModel(
        factory = DeliveriesListViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            navigationManager = NavigationManager(rememberNavController()),
            deliverySharedViewModel = DeliverySharedViewModel(),
        )
    )

    Theme.ArrivoTheme {
        DeliveriesListView(deliveriesListView)
    }
}