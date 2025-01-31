package com.thesis.arrivo.ui.user.user_map_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesis.arrivo.R
import com.thesis.arrivo.components.other_components.AppButton
import com.thesis.arrivo.components.other_components.EmptyList
import com.thesis.arrivo.utilities.navigation_api.NavigationApiManager
import com.thesis.arrivo.view_models.MapViewModel


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
            .padding(horizontal = 8.dp)
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
            text = "Start Break",
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

        TextData(text = "Drive Time:", color = labelColor)
        TextData(text = "Break:", color = labelColor)
    }
}

@Composable
private fun TextData(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 16.sp,
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
    Text(text = "Error: " + mapViewModel.errorMessage)
}


