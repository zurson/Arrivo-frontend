package com.thesis.arrivo.ui.admin.admin_accidents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.road_accidents.RoadAccident
import com.thesis.arrivo.components.animations.bounceClick
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.components.info_alert_dialog.InfoAlertDialog
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppFilter
import com.thesis.arrivo.components.other_components.ArrowRightIcon
import com.thesis.arrivo.components.other_components.Circle
import com.thesis.arrivo.components.other_components.ConfirmationDialog
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.components.other_components.GoogleMapView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.interfaces.LoadingScreenStatusChecker
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.RoadAccidentsViewModel
import com.thesis.arrivo.view_models.factory.RoadAccidentViewModelFactory

@Composable
fun AccidentsView(roadAccidentsViewModel: RoadAccidentsViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
    ) {
        AccidentDetailsDialog(roadAccidentsViewModel)
        MarkAsResolvedConfirmationDialog(roadAccidentsViewModel)
        AccidentLocationOnMap(roadAccidentsViewModel)

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* DATE AND FILTERS */
        val (dateAndFiltersRef) = createRefs()
        val dateAndFiltersTopGuideline = createGuidelineFromTop(0.05f)
        val dateAndFiltersBottomGuideline = createGuidelineFromTop(0.25f)

        DatePickerAndFilters(
            roadAccidentsViewModel = roadAccidentsViewModel,
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

        RoadAccidentsList(
            roadAccidentsViewModel = roadAccidentsViewModel,
            loadingScreenStatusChecker = roadAccidentsViewModel,
            modifier = Modifier.constrainAs(roadAccidentsListRef) {
                top.linkTo(roadAccidentsListTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}

/**
 * DATE AND FILTERS
 **/

@Composable
private fun DatePickerAndFilters(
    modifier: Modifier = Modifier,
    roadAccidentsViewModel: RoadAccidentsViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxSize()
    ) {
        DatePickerField(
            selectedDate = roadAccidentsViewModel.getSelectedDate(),
            onDateSelected = { roadAccidentsViewModel.onDateSelected(it) },
        )

        FiltersList(
            roadAccidentsViewModel = roadAccidentsViewModel,
        )
    }
}


@Composable
private fun FiltersList(
    roadAccidentsViewModel: RoadAccidentsViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        RoadAccidentStatus.entries.forEach { filter ->
            AppFilter(
                modifier = Modifier.weight(1f),
                filter = filter,
                isActive = roadAccidentsViewModel.getActiveFilters().contains(filter),
                filterToString = { roadAccidentsViewModel.filterToString(it) },
                onSelected = { roadAccidentsViewModel.toggleFilterActive(it) }
            )
        }
    }
}


/**
 * ROAD ACCIDENTS LIST
 **/

@Composable
private fun RoadAccidentsList(
    modifier: Modifier = Modifier,
    roadAccidentsViewModel: RoadAccidentsViewModel,
    loadingScreenStatusChecker: LoadingScreenStatusChecker
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (roadAccidentsViewModel.accidentsToShow.isEmpty()) {
            EmptyList(loadingScreenStatusChecker)
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        ) {
            items(roadAccidentsViewModel.accidentsToShow) { accident ->
                RoadAccidentContainer(
                    roadAccident = accident,
                    roadAccidentsViewModel = roadAccidentsViewModel
                )
            }
        }
    }
}


@Composable
private fun RoadAccidentContainer(
    roadAccidentsViewModel: RoadAccidentsViewModel,
    roadAccident: RoadAccident
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
            .clickable { roadAccidentsViewModel.onRoadAccidentSelected(roadAccident) }
            .padding(dimensionResource(R.dimen.accidents_container_padding))
    ) {
        Circle(
            size = dimensionResource(R.dimen.accidents_container_circle_icon_size),
            color = roadAccidentsViewModel.getIndicatorColor(roadAccident.status),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            AccidentParticipant(employee = roadAccident.employee)
            AccidentCategory(text = RoadAccidentsViewModel.formatAccidentCategory(roadAccident.category))
        }

        ArrowRightIcon(size = dimensionResource(R.dimen.accidents_container_arrow_right_icon_size))
    }

}


@Composable
private fun AccidentParticipant(
    employee: Employee,
) {
    Text(
        text = "${employee.firstName} ${employee.lastName}",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = dpToSp(R.dimen.accidents_container_participant_text_size),
        maxLines = 2,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
private fun AccidentCategory(
    text: String
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        fontSize = dpToSp(R.dimen.accidents_container_category_text_size),
        maxLines = 1,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
}


/**
 * OTHER
 **/


@Composable
private fun AccidentDetailsDialog(roadAccidentsViewModel: RoadAccidentsViewModel) {
    if (roadAccidentsViewModel.showAccidentDetailsDialog) {
        RoadAccidentDetailsDialog(
            accident = roadAccidentsViewModel.selectedAccident,
            onDismiss = { roadAccidentsViewModel.onAccidentDialogDismiss() },
            onButtonClick = { roadAccidentsViewModel.onAccidentMarkAsResolvedButtonClick() },
            onMapButtonClick = { roadAccidentsViewModel.onMapButtonClick() },
            onCallButtonClick = { roadAccidentsViewModel.onCallButtonClick() }
        )
    }
}


@Composable
private fun MarkAsResolvedConfirmationDialog(roadAccidentsViewModel: RoadAccidentsViewModel) {
    if (!roadAccidentsViewModel.showConfirmationDialog)
        return

    ConfirmationDialog(
        dialogTitle = "Confirmation",
        lineOne = "Mark accident as resolved?",
        lineTwo = null,
        onYesClick = { roadAccidentsViewModel.onConfirmationYesClick() },
        onNoClick = { roadAccidentsViewModel.onConfirmationNoClick() },
        onDismiss = { roadAccidentsViewModel.onConfirmationDismiss() },
    )
}


@Composable
private fun AccidentLocationOnMap(roadAccidentsViewModel: RoadAccidentsViewModel) {
    if (!roadAccidentsViewModel.showAccidentLocationOnMap)
        return

    InfoAlertDialog(
        title = stringResource(R.string.accidents_location_dialog_title),
        onDismiss = { roadAccidentsViewModel.onAccidentLocationMapDismiss() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space))
        ) {
            GoogleMapView(
                selectedLocation = roadAccidentsViewModel.selectedAccident.location.toLatLon(),
                modifier = Modifier
                    .height(dimensionResource(R.dimen.accidents_dialog_map_height))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius))
                    )
            )

            AppButton(
                text = stringResource(R.string.accident_location_dialog_dismiss_button_text),
                onClick = { roadAccidentsViewModel.onAccidentLocationMapDismiss() }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm = MainScaffoldViewModel(
        navigationManager = NavigationManager(rememberNavController())
    )

    val viewModel: RoadAccidentsViewModel = viewModel(
        factory = RoadAccidentViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm
        )
    )

    Theme.ArrivoTheme {
        AccidentsView(viewModel)
    }
}