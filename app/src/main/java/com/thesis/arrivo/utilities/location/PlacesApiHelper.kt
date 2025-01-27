package com.thesis.arrivo.utilities.location

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.thesis.arrivo.activities.MainActivity
import java.util.Locale

object PlacesApiHelper {

    private lateinit var placesClient: PlacesClient

    fun init(placesClient: PlacesClient) {
        PlacesApiHelper.placesClient = placesClient
    }


    fun fetchAddressFromCoordinates(location: Location, callback: (String?) -> Unit) {
        val geocoder = Geocoder(MainActivity.context, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNullOrEmpty()) {
                callback(null)
            } else {
                val address = addresses[0].getAddressLine(0)
                callback(address)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }


    fun fetchPredictions(query: String, callback: (List<AutocompletePrediction>) -> Unit) {
        val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            callback(response.autocompletePredictions)
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }


    fun fetchLocationFromPlaceId(placeId: String, callback: (LatLng?) -> Unit) {
        val request = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG))

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            callback(response.place.location)
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(null)
        }
    }
}