package com.thesis.arrivo.view_models

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.CameraPositionState
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.communication.available_products.AvailableProduct
import com.thesis.arrivo.communication.available_products.AvailableProductsRepository
import com.thesis.arrivo.communication.task.Product
import com.thesis.arrivo.communication.task.TaskCreateRequest
import com.thesis.arrivo.communication.task.TaskUpdateRequest
import com.thesis.arrivo.communication.task.TasksRepository
import com.thesis.arrivo.components.navigation.NavigationItem
import com.thesis.arrivo.utilities.NavigationManager
import com.thesis.arrivo.utilities.Settings.Companion.DEFAULT_MAP_ZOOM
import com.thesis.arrivo.utilities.capitalize
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.location.PlacesApiHelper
import com.thesis.arrivo.utilities.showToast
import kotlinx.coroutines.launch

class TaskManagerViewModel(
    private val context: Context,
    private val mainViewModel: MainViewModel,
    loadingScreenManager: LoadingScreenManager,
    private val navigationManager: NavigationManager,
) : ViewModel() {
    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)

    private val tasksRepository: TasksRepository by lazy { TasksRepository() }
    private val availableProductsRepository: AvailableProductsRepository by lazy { AvailableProductsRepository() }

    companion object {
        val DEFAULT_LOCATION: LatLng = LatLng(52.2370, 21.0175)

        private var _availableProducts: List<AvailableProduct> = emptyList()
        private val availableProducts: List<AvailableProduct>
            get() = _availableProducts
    }


    /**
     * Available products
     **/


    fun getAvailableProducts(): List<AvailableProduct> = availableProducts


    private fun fetchAvailableProducts() {
        if (availableProducts.isNotEmpty())
            return

        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    _availableProducts = availableProductsRepository.getAllAvailableProducts()
                }
            )
        }
    }

    init {
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
    private var selectedProductName by mutableStateOf("")
    var isProductSpinnerError by mutableStateOf(false)
    var isProductAmountError by mutableStateOf(false)

    private val _selectedProduct = mutableStateOf(AvailableProduct.emptyProduct())
    val selectedProduct: AvailableProduct
        get() = _selectedProduct.value

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
        _selectedProduct.value = AvailableProduct.emptyProduct()
    }


    private fun toggleShowProductDeleteConfirmationDialog() {
        _showDeleteConfirmationDialog.value = !_showDeleteConfirmationDialog.value
    }


    fun onProductSelected(product: AvailableProduct) {
        selectedProductName = product.name
        isProductSpinnerError = false

        _selectedProduct.value = product
    }


    fun productToString(product: AvailableProduct): String = product.name


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


    fun onProductDeleteConfirmationDismiss() {
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

    private var isLocationSelected: Boolean = false
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


    private fun setSelectedLocation(location: LatLng?) {
        _selectedLocation.value = location ?: DEFAULT_LOCATION
    }


    private fun getCameraPosition() =
        CameraPosition.fromLatLngZoom(selectedLocation, DEFAULT_MAP_ZOOM)


    fun toggleLocationSearchDialog() {
        _showLocationSearchDialog.value = !_showLocationSearchDialog.value
    }


    private fun clearPredictions() {
        _predictions.clear()
    }


    private fun fetchPredictions(query: String) {
        PlacesApiHelper.fetchPredictions(query = query, callback = { result ->
            clearPredictions()
            predictions.addAll(result)
        })
    }


    private fun fetchLocationFromPlaceId(placeId: String, callback: (LatLng?) -> Unit) {
        PlacesApiHelper.fetchLocationFromPlaceId(placeId = placeId, callback = { result ->
            callback(result)
        })
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
     * Create or Save Task
     **/


    var taskTitleError by mutableStateOf(false)
    var deliveryAddressError by mutableStateOf(false)

    fun onButtonClick(editMode: Boolean) {
        if (!validateConditions())
            return

        if (editMode)
            sendTaskUpdateRequest(editMode)
        else
            sendTaskCreateRequest(editMode)
    }


    private fun validateConditions(): Boolean {
        if (taskTitle.isEmpty()) {
            taskTitleError = true
            return false
        }

        if (!isLocationSelected) {
            deliveryAddressError = true
            return false
        }

        return true
    }


    private fun sendTaskCreateRequest(editMode: Boolean) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    tasksRepository.createTask(createTaskCreateRequest())
                },
                onSuccess = { onSuccess(editMode) }
            )
        }
    }


    private fun sendTaskUpdateRequest(editMode: Boolean) {
        viewModelScope.launch {
            serverRequestManager.sendRequest(
                actionToPerform = {
                    tasksRepository.updateTask(
                        id = mainViewModel.taskToEdit.task.id,
                        taskUpdateRequest = createTaskUpdateRequest()
                    )
                },
                onSuccess = { onSuccess(editMode) }
            )
        }
    }


    private fun createTaskCreateRequest(): TaskCreateRequest {
        return TaskCreateRequest(
            title = taskTitle,
            location = Location(
                latitude = selectedLocation.latitude,
                longitude = selectedLocation.longitude
            ),
            addressText = finalAddress,
            products = products
        )
    }


    private fun createTaskUpdateRequest(): TaskUpdateRequest {
        return TaskUpdateRequest(
            title = taskTitle,
            location = Location(
                latitude = selectedLocation.latitude,
                longitude = selectedLocation.longitude
            ),
            addressText = finalAddress,
            products = products,
        )
    }


    private fun onSuccess(editMode: Boolean) {
        val messageId =
            if (editMode)
                R.string.task_create_or_edit_edit_success_message
            else
                R.string.task_create_or_edit_create_success_message

        showToast(
            text = context.getString(messageId),
            toastLength = Toast.LENGTH_LONG,
        )

        navigationManager.navigateTo(
            routeOrItem = NavigationItem.TasksListAdmin,
            clearHistory = true
        )
    }


    /**
     * Task Edit Mode
     **/


    fun prepareToEdit() {
        val taskToEdit = mainViewModel.taskToEdit
        val task = taskToEdit.task

        taskTitle = task.title
        products = task.products.toMutableList()
        finalAddress = task.addressText
        query = task.addressText
        setSelectedLocation(task.location.toLatLon())
        isLocationSelected = true
    }


    /**
     * Other
     **/


    fun getFullAddress(prediction: AutocompletePrediction): String {
        val fullAddress = prediction.getFullText(null).toString()
        return fullAddress.substringBeforeLast(",").trim()
    }

}
