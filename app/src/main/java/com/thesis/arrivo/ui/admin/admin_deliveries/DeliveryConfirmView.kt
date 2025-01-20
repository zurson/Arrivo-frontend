package com.thesis.arrivo.ui.admin.admin_deliveries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppHorizontalDivider
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.view_models.DeliveryConfirmViewModel
import com.thesis.arrivo.view_models.DeliverySharedViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.DeliveryConfirmViewModelFactory

@Composable
fun DeliveryCreateView(deliveryConfirmViewModel: DeliveryConfirmViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* MAIN SECTION */
        val (mainSectionRef) = createRefs()
        val mainSectionTopGuideline = createGuidelineFromTop(0.05f)
        val mainSectionBottomGuideline = createGuidelineFromTop(0.85f)

        MainSection(
            deliveryConfirmViewModel = deliveryConfirmViewModel,
            modifier = Modifier.constrainAs(mainSectionRef) {
                top.linkTo(mainSectionTopGuideline)
                bottom.linkTo(mainSectionBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /* BUTTONS */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.86f)

        ButtonsSection(
            deliveryConfirmViewModel = deliveryConfirmViewModel,
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
private fun MainSection(
    deliveryConfirmViewModel: DeliveryConfirmViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.delivery_order_list_title),
            fontSize = dpToSp(R.dimen.delivery_order_list_title_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        TasksList(
            deliveryConfirmViewModel = deliveryConfirmViewModel,
            modifier = Modifier.weight(1f)
        )

        DetailsSection(
            timeText = deliveryConfirmViewModel.getPredictedTimeText(),
            textColor = deliveryConfirmViewModel.getPredictedTimeTextColor(),
            distanceKm = deliveryConfirmViewModel.distanceKm
        )
    }
}


@Composable
private fun TasksList(
    deliveryConfirmViewModel: DeliveryConfirmViewModel,
    modifier: Modifier = Modifier
) {
    val data = deliveryConfirmViewModel.selectedTasks

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        modifier = modifier
    ) {

        if (deliveryConfirmViewModel.isLoadingScreenEnabled())
            return@LazyColumn

        items(data) { task ->
            TaskContainer(task = task, position = data.indexOf(task) + 1)
        }
    }
}


@Composable
private fun TaskContainer(
    task: Task,
    position: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(dimensionResource(R.dimen.delivery_available_tasks_container_padding))

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

        Text(
            text = "$position",
            fontSize = dpToSp(R.dimen.delivery_order_container_position_text_size),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.delivery_order_container_position_end_padding))
        )

    }
}


@Composable
private fun DetailsSection(
    timeText: String,
    distanceKm: Int,
    textColor: Color
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AppHorizontalDivider(
            height = dimensionResource(R.dimen.delivery_order_horizontal_divider_height),
            Modifier.padding(bottom = dimensionResource(R.dimen.delivery_order_horizontal_divider_padding))
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PredictedTimeInfo(
                timeText = timeText,
                textColor = textColor,
                modifier = Modifier.weight(1f)
            )

            PredictedDistanceInfo(
                distanceKm = distanceKm,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun PredictedTimeInfo(
    timeText: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.delivery_order_predicted_time_title),
            fontSize = dpToSp(R.dimen.delivery_order_details_title_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = timeText,
            fontSize = dpToSp(R.dimen.delivery_order_details_text_size),
            color = textColor
        )
    }
}


@Composable
private fun PredictedDistanceInfo(
    distanceKm: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.delivery_order_predicted_distance_title),
            fontSize = dpToSp(R.dimen.delivery_order_details_title_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "$distanceKm km",
            fontSize = dpToSp(R.dimen.delivery_order_details_text_size),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
private fun ButtonsSection(
    modifier: Modifier = Modifier,
    deliveryConfirmViewModel: DeliveryConfirmViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        modifier = modifier
            .fillMaxSize(),
    ) {
        AppButton(
            onClick = { deliveryConfirmViewModel.onBackButtonClick() },
            text = stringResource(R.string.delivery_order_back_button_text),
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            iconStart = true,
            modifier = Modifier.weight(1f)
        )

        AppButton(
            onClick = { deliveryConfirmViewModel.onFinishButtonClick() },
            text = stringResource(R.string.delivery_order_back_finish_text),
            icon = Icons.Filled.Check,
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainScaffoldViewModel(
        navigationManager = NavigationManager(rememberNavController()),
        context = LocalContext.current,
        adminMode = true
    )

    val deliveryConfirmViewModel: DeliveryConfirmViewModel = viewModel(
        factory = DeliveryConfirmViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            navigationManager = NavigationManager(rememberNavController()),
            deliverySharedViewModel = DeliverySharedViewModel()
        )
    )

    Theme.ArrivoTheme {
        DeliveryCreateView(deliveryConfirmViewModel)
    }
}