package com.thesis.arrivo.ui.admin.admin_tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

//@Composable
//fun TasksView(modifier: Modifier = Modifier) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        Text(
//            text = "TasksView ADMIN",
//            color = MaterialTheme.colorScheme.primary,
//            fontSize = 24.sp
//        )
//    }
//}


@Composable
fun TasksView(placesClient: PlacesClient) {
    var query by remember { mutableStateOf("") }
    val predictions = remember { mutableStateListOf<AutocompletePrediction>() }
    val selectedLocation = remember { mutableStateOf<LatLng?>(null) }

    // Kamera dla mapy, która będzie aktualizowana po wybraniu miejsca
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation.value ?: LatLng(0.0, 0.0), 15f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = { newValue ->
                query = newValue
                if (newValue.length > 2) {
                    fetchPredictions(newValue, placesClient, predictions)
                }
            },
            placeholder = { Text("Szukaj miejsca...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        LazyColumn {
            items(predictions) { prediction ->
                Text(
                    prediction.getPrimaryText(null).toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Wybór miejsca i aktualizacja kamery
                            fetchLocationFromPlaceId(prediction.placeId, placesClient) { location ->
                                selectedLocation.value = location
                                // Aktualizacja kamery
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(location ?: LatLng(0.0, 0.0), 15f)
                            }
                            predictions.clear()
                        }
                        .padding(8.dp)
                )
            }
        }

        // MapView z aktualizowaną kamerą
        GoogleMapView(selectedLocation = selectedLocation.value, cameraPositionState = cameraPositionState)
    }
}

fun fetchPredictions(
    query: String,
    placesClient: PlacesClient,
    predictions: MutableList<AutocompletePrediction>
) {
    val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        predictions.clear()
        predictions.addAll(response.autocompletePredictions)
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
}

fun fetchLocationFromPlaceId(
    placeId: String,
    placesClient: PlacesClient,
    callback: (LatLng?) -> Unit
) {
    val request = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG))
    placesClient.fetchPlace(request).addOnSuccessListener { response ->
        callback(response.place.latLng)
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
        callback(null)
    }
}

@Composable
fun GoogleMapView(selectedLocation: LatLng?, cameraPositionState: CameraPositionState) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        selectedLocation?.let {
            Marker(
                state = MarkerState(position = it)  // Używamy MarkerState
            )
        }
    }
}