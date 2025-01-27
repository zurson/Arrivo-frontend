package com.thesis.arrivo.ui.user.user_accident_report_view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.info_alert_dialog.DialogRecord
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.AppSpinner
import com.thesis.arrivo.components.other_components.AppTextField
import com.thesis.arrivo.components.other_components.GoogleMapView
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.view_models.AccidentReportViewModel
import com.thesis.arrivo.view_models.MainScaffoldViewModel
import com.thesis.arrivo.view_models.factory.AccidentReportViewModelFactory

@Composable
fun AccidentReportView(accidentReportViewModel: AccidentReportViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(Settings.START_END_PERCENTAGE)
        val endGuideline = createGuidelineFromEnd(Settings.START_END_PERCENTAGE)

        /* ACCIDENT LOCATION */
        val (accidentLocationRef) = createRefs()
        val accidentLocationTopGuideline = createGuidelineFromTop(0.05f)
        val accidentLocationBottomGuideline = createGuidelineFromTop(0.30f)

        AccidentLocation(
            accidentReportViewModel = accidentReportViewModel,
            modifier = Modifier.constrainAs(accidentLocationRef) {
                top.linkTo(accidentLocationTopGuideline)
                bottom.linkTo(accidentLocationBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })

        /* ACCIDENT DETAILS SECTION */
        val (detailsRef) = createRefs()
        val detailsTopGuideline = createGuidelineFromTop(0.35f)
        val detailsBottomGuideline = createGuidelineFromTop(0.89f)

        AccidentDetailsSection(
            accidentReportViewModel = accidentReportViewModel,
            modifier = Modifier.constrainAs(detailsRef) {
                top.linkTo(detailsTopGuideline)
                bottom.linkTo(detailsBottomGuideline)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })


        /* BUTTON SECTOR */
        val (buttonRef) = createRefs()
        val buttonTopGuideline = createGuidelineFromTop(0.9f)

        ConfirmButton(
            accidentReportViewModel = accidentReportViewModel,
            modifier = Modifier.constrainAs(buttonRef) {
                top.linkTo(buttonTopGuideline)
                bottom.linkTo(parent.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
    }
}


@Composable
private fun AccidentLocation(modifier: Modifier, accidentReportViewModel: AccidentReportViewModel) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius)))
            .border(
                width = dimensionResource(R.dimen.accident_report_map_border_width),
                color = MaterialTheme.colorScheme.primary,
                RoundedCornerShape(dimensionResource(R.dimen.surfaces_corner_clip_radius))
            )
    ) {
        GoogleMapView(selectedLocation = accidentReportViewModel.currentLocation.toLatLon())
    }
}


@Composable
private fun AccidentDetailsSection(
    accidentReportViewModel: AccidentReportViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        item { LocationItem(accidentReportViewModel.address) }
        item { TimeItem(accidentReportViewModel.getTimeText()) }
        item { CarIdItem(accidentReportViewModel) }
        item { CategoryItem(accidentReportViewModel) }
        item { DescriptionItem(accidentReportViewModel) }
    }
}


@Composable
private fun LocationItem(address: String) {
    DialogRecord(
        label = stringResource(R.string.accident_report_address_label),
        value = address,
        maxLines = 2
    )
}

@Composable
private fun TimeItem(timeText: String) {
    DialogRecord(
        label = stringResource(R.string.accident_report_time_label),
        value = timeText
    )
}

@Composable
private fun CarIdItem(accidentReportViewModel: AccidentReportViewModel) {
    AppTextField(
        value = accidentReportViewModel.carId,
        onValueChange = { accidentReportViewModel.onCarIdValueChange(it) },
        label = stringResource(R.string.accident_report_car_id_label),
        trailingIcon = Icons.Filled.Close,
        onTrailingIconClick = { accidentReportViewModel.onCarIdTrailingIconClick() },
        maxLength = Settings.ACCIDENT_REPORT_CAR_ID_MAX_LEN,
        isError = accidentReportViewModel.carIdError,
        errorMessage = stringResource(R.string.accident_report_error_car_id),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CategoryItem(accidentReportViewModel: AccidentReportViewModel) {
    AppSpinner(
        items = accidentReportViewModel.getAvailableCategories(),
        label = stringResource(R.string.accident_report_category_label),
        selectedItem = accidentReportViewModel.selectedCategory,
        onItemSelected = { accidentReportViewModel.onCategorySelected(it) },
        itemToString = { accidentReportViewModel.categoryToString(it) }
    )
}

@Composable
private fun DescriptionItem(accidentReportViewModel: AccidentReportViewModel) {
    AppTextField(
        value = accidentReportViewModel.description,
        onValueChange = { accidentReportViewModel.onDescriptionValueChange(it) },
        label = stringResource(R.string.accident_report_description_label),
        singleLine = false,
        imeAction = ImeAction.Default,
        maxLines = Int.MAX_VALUE,
        trailingIcon = Icons.Filled.Close,
        onTrailingIconClick = { accidentReportViewModel.onDescriptionTrailingIconClick() },
        maxLength = Settings.ACCIDENT_REPORT_MAX_DESCRIPTION_LEN,
        isError = accidentReportViewModel.descriptionError,
        errorMessage = stringResource(R.string.accident_report_error_description),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(dimensionResource(R.dimen.accident_report_description_default_height))
    )
}


@Composable
private fun ConfirmButton(
    accidentReportViewModel: AccidentReportViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        AppButton(
            onClick = { accidentReportViewModel.onConfirmButtonClick() },
            text = stringResource(R.string.accident_report_confirm_button_text)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val mainVm = MainScaffoldViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val vm: AccidentReportViewModel = viewModel(
        factory = AccidentReportViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            loggedInUserAccessor = mainVm,
            navigationManager = NavigationManager(rememberNavController())
        )
    )

    Theme.ArrivoTheme {
        AccidentReportView(vm)
    }
}