package com.thesis.arrivo.ui.user.user_map_view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.thesis.arrivo.R
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

        NavigationView()
    }
}


@Composable
private fun NavigationView() {
    NavigationApiManager.NavigationView()
}


@Composable
private fun NoDestinationView(mapViewModel: MapViewModel) {
//    EmptyList(
//
//    )
}


@Composable
private fun ErrorView(mapViewModel: MapViewModel) {
    Text(text = "Error: " + mapViewModel.errorMessage)
}


