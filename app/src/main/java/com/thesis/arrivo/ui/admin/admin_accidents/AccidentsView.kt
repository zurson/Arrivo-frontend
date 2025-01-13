package com.thesis.arrivo.ui.admin.admin_accidents

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.employee.Employee
import com.thesis.arrivo.communication.road_accidents.RoadAccident
import com.thesis.arrivo.components.ArrowRightIcon
import com.thesis.arrivo.components.Circle
import com.thesis.arrivo.components.EmptyList
import com.thesis.arrivo.components.LoadingScreen
import com.thesis.arrivo.components.bounceClick
import com.thesis.arrivo.components.date_picker.DatePickerField
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.RoadAccidentsViewModel

@Composable
fun AccidentsView(mainScaffoldViewModel: MainScaffoldViewModel) {
    val context = LocalContext.current
    val roadAccidentsViewModel = remember { RoadAccidentsViewModel(context) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
    ) {
        ShowAccidentDetailsDialog(roadAccidentsViewModel)

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
            modifier = Modifier.constrainAs(roadAccidentsListRef) {
                top.linkTo(roadAccidentsListTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        LoadingScreen(enabled = roadAccidentsViewModel.showLoadingScreen)
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
        Filter(
            filter = RoadAccidentStatus.ACTIVE,
            modifier = Modifier.weight(1f),
            roadAccidentsViewModel = roadAccidentsViewModel
        )

        Filter(
            filter = RoadAccidentStatus.ENDED,
            modifier = Modifier.weight(1f),
            roadAccidentsViewModel = roadAccidentsViewModel
        )
    }
}


@Composable
private fun Filter(
    modifier: Modifier = Modifier,
    filter: RoadAccidentStatus,
    roadAccidentsViewModel: RoadAccidentsViewModel
) {
    val active = roadAccidentsViewModel.getActiveFilters().contains(filter)
    val color =
        if (active) Settings.FILTER_ACTIVE_COLOR else MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .bounceClick()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(color)
            .clickable { roadAccidentsViewModel.toggleFilterActive(filter) }
    ) {
        Text(
            text = capitalize(filter.name.lowercase()),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dpToSp(R.dimen.accidents_filter_text_size),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.accidents_filter_padding))
        )
    }
}


/**
 * ROAD ACCIDENTS LIST
 **/

@Composable
private fun RoadAccidentsList(
    modifier: Modifier = Modifier,
    roadAccidentsViewModel: RoadAccidentsViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (roadAccidentsViewModel.accidentsToShow.isEmpty()) {
            EmptyList()
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
private fun ShowAccidentDetailsDialog(roadAccidentsViewModel: RoadAccidentsViewModel) {
    if (roadAccidentsViewModel.showAccidentDetailsDialog) {
        RoadAccidentDetailsDialog(
            accident = roadAccidentsViewModel.selectedAccident,
            onDismiss = { roadAccidentsViewModel.onAccidentDialogDismiss() },
            onEditButtonClick = { roadAccidentsViewModel.onAccidentFinishButtonClick() }
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Theme.ArrivoTheme {
        AccidentsView(
            mainScaffoldViewModel = MainScaffoldViewModel(
                navController = rememberNavController(),
                context = LocalContext.current,
                adminMode = true
            )
        )
    }
}