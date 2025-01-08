package com.thesis.arrivo.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.thesis.arrivo.ui.admin.admin_tasks.Product
import com.thesis.arrivo.communication.available_products.AvailableProduct
import com.thesis.arrivo.utilities.capitalize

class NewTaskViewModel(
    val placesClient: PlacesClient,
) : ViewModel() {

    companion object {
        val DEFAULT_LOCATION: LatLng = LatLng(52.2370, 21.0175)
        const val DEFAULT_ZOOM: Float = 17f
    }


    /**
     * Available products
     **/

    private var _availableProducts: List<AvailableProduct> = emptyList()
    val availableProducts: List<AvailableProduct>
        get() = _availableProducts

    private fun fetchAvailableProducts() {
        val mockProducts = listOf(
            AvailableProduct("Apple juice"),
            AvailableProduct("Orange juice"),
            AvailableProduct("Plump juice"),
            AvailableProduct("Grape juice"),
            AvailableProduct("Peach juice"),
            AvailableProduct("Strawberry juice"),
            AvailableProduct("Empty boxes"),
            AvailableProduct("Glasses"),
            AvailableProduct("Air fryer"),
            AvailableProduct("Cooker"),
            AvailableProduct("Fridge"),

        )

        _availableProducts = mockProducts
    }


    init {
        if (availableProducts.isEmpty())
            fetchAvailableProducts()
    }


    /**
     * Add product
     **/

    private val _showAddProductDialog = mutableStateOf(false)
    val showAddProductDialog: Boolean
        get() = _showAddProductDialog.value


    private val _showDeleteConfirmationDialog = mutableStateOf(false)
    val showDeleteConfirmationDialog: Boolean
        get() = _showDeleteConfirmationDialog.value

    var productToDelete by mutableStateOf<Product?>(null)

    var selectedProductAmount by mutableStateOf("")
    var selectedProductName by mutableStateOf("")
    var isProductSpinnerError by mutableStateOf(false)
    var isProductAmountError by mutableStateOf(false)

    fun onProductAddButtonClick() {
        if (selectedProductName.isEmpty()) {
            isProductSpinnerError = true
            return
        }

        try {
            val amount = selectedProductAmount.toInt()

            products.add(
                Product(
                    name = selectedProductName,
                    amount = amount
                )
            )

            toggleShowAddProductDialog()
        } catch (e: NumberFormatException) {
            isProductAmountError = true
        }


    }


    fun onNewProductButtonClick() {
        toggleShowAddProductDialog()
        selectedProductName = ""
        selectedProductAmount = ""

        isProductSpinnerError = false
        isProductAmountError = false
    }


    fun toggleShowAddProductDialog() {
        _showAddProductDialog.value = !_showAddProductDialog.value
    }


    fun toggleShowProductDeleteConfirmationDialog() {
        _showDeleteConfirmationDialog.value = !_showDeleteConfirmationDialog.value
    }


    fun onProductSelected(item: String) {
        selectedProductName = item
        isProductSpinnerError = false
    }


    fun onProductAmountValueChange(value: String) {
        selectedProductAmount = value
    }


    fun onProductDelete(product: Product) {
        productToDelete = product
        toggleShowProductDeleteConfirmationDialog()
    }


    fun onProductDeleteConfirmationYesClick() {
        _products.remove(productToDelete)
        toggleShowProductDeleteConfirmationDialog()
    }


    fun onProductDeleteConfirmationNoClick() {
        toggleShowProductDeleteConfirmationDialog()
    }

    /**
     * Task title
     **/


    var taskTitle by mutableStateOf("")


    fun onTaskTitleChange(newValue: String) {
        taskTitle = capitalize(newValue)
        taskTitleError = false
    }


    /**
     * Products
     **/

    private val _products = mutableStateListOf<Product>()
    var products: MutableList<Product>
        get() = _products
        set(value) {
            _products.clear()
            _products.addAll(value)
        }


    /**
     * Location search
     **/

    private val _showLocationSearchDialog = mutableStateOf(false)
    val showLocationSearchDialog: Boolean
        get() = _showLocationSearchDialog.value

    var query by mutableStateOf("")
    var finalAddress by mutableStateOf("")

    var isLocationSelected: Boolean = false
    var locationSearchBarError by mutableStateOf(false)

    private val _selectedLocation = mutableStateOf(DEFAULT_LOCATION)
    val selectedLocation: LatLng
        get() = _selectedLocation.value


    private val _predictions = mutableStateListOf<AutocompletePrediction>()
    var predictions: MutableList<AutocompletePrediction>
        get() = _predictions
        set(value) {
            clearPredictions()
            _predictions.addAll(value)
        }


    fun setSelectedLocation(location: LatLng?) {
        _selectedLocation.value = location ?: DEFAULT_LOCATION
    }


    fun getCameraPosition() = CameraPosition.fromLatLngZoom(selectedLocation, DEFAULT_ZOOM)


    fun toggleLocationSearchDialog() {
        _showLocationSearchDialog.value = !_showLocationSearchDialog.value
    }


    fun clearPredictions() {
        _predictions.clear()
    }


    private fun fetchPredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            clearPredictions()
            predictions = response.autocompletePredictions
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }


    private fun fetchLocationFromPlaceId(
        placeId: String, callback: (LatLng?) -> Unit
    ) {
        val request = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG))

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            callback(response.place.location)
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(null)
        }
    }


    fun onSearchBarValueChange(newValue: String) {
        query = newValue
        locationSearchBarError = false
        isLocationSelected = false

        if (newValue.length > 2)
            fetchPredictions(newValue)
    }


    fun onLocationSearchBarTrailingIconClick() {
        query = ""
        isLocationSelected = false
        clearPredictions()
    }


    fun onAddressClick(
        prediction: AutocompletePrediction,
        cameraPositionState: CameraPositionState
    ) {
        fetchLocationFromPlaceId(
            prediction.placeId,
        ) { location ->
            setSelectedLocation(location)
            cameraPositionState.position = getCameraPosition()
        }

        query = getFullAddress(prediction)
        isLocationSelected = true
        clearPredictions()
    }


    fun onSelectLocationButtonClick() {
        if (!isLocationSelected) {
            locationSearchBarError = true
            return
        }

        finalAddress = query
        deliveryAddressError = false
        toggleLocationSearchDialog()
    }


    fun onOpenSearchLocationDialogButtonClick() {
        toggleLocationSearchDialog()
    }


    /**
     * Create Task
     **/

    var taskTitleError by mutableStateOf(false)
    var deliveryAddressError by mutableStateOf(false)

    fun onTaskCreateButtonClick() {
        if (taskTitle.isEmpty()) {
            taskTitleError = true
            return
        }

        if (!isLocationSelected) {
            deliveryAddressError = true
            return
        }


    }


    /**
     * Other
     **/

    fun getFullAddress(prediction: AutocompletePrediction): String {
        val fullAddress = prediction.getFullText(null).toString()
        return fullAddress.substringBeforeLast(",").trim()
    }

}
