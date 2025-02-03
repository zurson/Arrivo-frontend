package com.thesis.arrivo.ui.user.user_map_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.ui.theme.Theme
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.dpToSp
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager
import com.thesis.arrivo.view_models.MainViewModel
import com.thesis.arrivo.view_models.MapViewModel
import com.thesis.arrivo.view_models.factory.MapViewModelFactory


@Composable
fun MapView(mapViewModel: MapViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        if (mapViewModel.noDestination) {
            NoDestinationView(mapViewModel)
            return
        }

        if (mapViewModel.isError()) {
            ErrorView(mapViewModel)
            return
        }

        NavigationView(mapViewModel = mapViewModel)
    }
}


@Composable
private fun NavigationView(mapViewModel: MapViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_vertical_space)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavigationApiManager.NavigationView(modifier = Modifier.weight(1f))
        BottomSector(mapViewModel = mapViewModel)
    }
}


@Composable
private fun BottomSector(mapViewModel: MapViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.lists_elements_horizontal_space))
            .padding(horizontal = dimensionResource(R.dimen.navigation_bottom_sector_horizontal_padding))
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            TimeSection(mapViewModel = mapViewModel)
        }

        AppButton(
            onClick = { mapViewModel.onBreakButtonClick() },
            text = stringResource(R.string.break_start_button_text),
            icon = Icons.Outlined.Coffee,
            enabled = mapViewModel.breakButtonEnabled,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun TimeSection(mapViewModel: MapViewModel, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.lists_elements_horizontal_space)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        LabelsContainer(modifier = Modifier.wrapContentWidth())
        TimeContainer(mapViewModel = mapViewModel, modifier = Modifier.weight(1f))
    }
}


@Composable
fun TimeContainer(mapViewModel: MapViewModel, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        val timeColor = MaterialTheme.colorScheme.onBackground

        TextData(text = mapViewModel.driveTime, color = timeColor)
        TextData(text = mapViewModel.breakInTime, color = timeColor)
    }
}


@Composable
private fun LabelsContainer(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        val labelColor = MaterialTheme.colorScheme.primary

        TextData(text = stringResource(R.string.break_drive_time_text), color = labelColor)
        TextData(text = stringResource(R.string.break_break_in_text), color = labelColor)
    }
}

@Composable
private fun TextData(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = dpToSp(R.dimen.navigation_bottom_sector_text_size),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start,
        color = color,
        modifier = modifier
    )
}


@Composable
private fun NoDestinationView(mapViewModel: MapViewModel) {
    EmptyList(
        loadingScreenStatusChecker = mapViewModel,
        stringResourceId = R.string.navigation_no_destination_message,
        icon = Icons.Outlined.LocationOn,
    )
}


@Composable
private fun ErrorView(mapViewModel: MapViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.navigation_error_padding))
    ) {
        Text(
            text = stringResource(R.string.navigation_error_title_text),
            color = MaterialTheme.colorScheme.primary,
            fontSize = dpToSp(R.dimen.navigation_error_title_text_size)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lists_elements_vertical_space)))

        Text(
            text = mapViewModel.errorMessage,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.navigation_error_code_text_size)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lists_elements_vertical_space)))

        AppButton(
            onClick = { mapViewModel.load() },
            text = stringResource(R.string.navigation_error_refresh_button_text)
        )
    }
}


@Preview
@Composable
private fun Preview() {
    val mainVm = MainViewModel(
        context = LocalContext.current,
        navigationManager = NavigationManager(rememberNavController())
    )

    val vm: MapViewModel = viewModel(
        factory = MapViewModelFactory(
            context = LocalContext.current,
            loadingScreenManager = mainVm,
            mapSharedViewModel = viewModel()
        )
    )

    Theme.ArrivoTheme {
        ErrorView(vm)
    }
}


