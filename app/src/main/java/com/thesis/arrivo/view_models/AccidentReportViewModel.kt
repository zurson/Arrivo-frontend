package com.thesis.arrivo.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thesis.arrivo.R
import com.thesis.arrivo.communication.ServerRequestManager
import com.thesis.arrivo.ui.common.road_accidents_list.RoadAccidentCategory
import com.thesis.arrivo.utilities.Settings
import com.thesis.arrivo.utilities.Settings.Companion.ACCIDENT_REPORT_MAX_DESCRIPTION_LEN
import com.thesis.arrivo.utilities.getCurrentTimeText
import com.thesis.arrivo.utilities.interfaces.LoadingScreenManager
import com.thesis.arrivo.utilities.location.Location
import com.thesis.arrivo.utilities.location.LocationHelper
import com.thesis.arrivo.utilities.location.PlacesApiHelper
import com.thesis.arrivo.utilities.showDefaultErrorDialog
import kotlinx.coroutines.launch

class AccidentReportViewModel(
    private val context: Context,
    private val loadingScreenManager: LoadingScreenManager
) : ViewModel() {

    private val serverRequestManager = ServerRequestManager(context, loadingScreenManager)


    /**
     * Current Location
     **/


    private val locationHelper = LocationHelper(context)

    private val _currentLocation = mutableStateOf(Location())
    val currentLocation: Location
        get() = _currentLocation.value


    private var locationFound: Boolean = false


    private fun setupCurrentLocation() {
        viewModelScope.launch {
            loadingScreenManager.showLoadingScreen()

            locationHelper.getCurrentLocation(
                onSuccess = { location -> onGetLocationSuccess(location) },
                onFailure = { exception -> onGetLocationFailure(exception) }
            )
        }
    }


    private fun onGetLocationSuccess(location: Location) {
        _currentLocation.value = location
        locationFound = true

        findAddress {
            loadingScreenManager.hideLoadingScreen()
        }
    }


    private fun onGetLocationFailure(ex: Exception) {
        loadingScreenManager.hideLoadingScreen()

        locationFound = false
        showDefaultErrorDialog(
            context = context,
            title = context.getString(R.string.error_title),
            message = ex.message ?: context.getString(R.string.unexpected_error)
        )
    }


    /**
     * Forms - Location
     **/

    private val _address = mutableStateOf("")
    val address: String
        get() = _address.value

    private fun findAddress(callback: () -> Unit) {
        PlacesApiHelper.fetchAddressFromCoordinates(
            location = _currentLocation.value,
            callback = { address ->
                if (address == null)
                    _address.value = context.getString(R.string.accident_report_undefined_location_text)
                else
                    _address.value = removeCountryFromAddress(address)

                callback()
            })
    }


    private fun removeCountryFromAddress(address: String): String {
        val addressParts = address.split(",").map { it.trim() }

        return if (addressParts.size > 1) {
            addressParts.dropLast(1).joinToString(", ")
        } else {
            ""
        }
    }


    /**
     * Forms - Time
     **/


    fun getTimeText(): String {
        return getCurrentTimeText()
    }


    /**
     * Forms - CAR ID
     **/

    private val _carIdError = mutableStateOf(false)
    val carIdError: Boolean
        get() = _carIdError.value

    private val _carId = mutableStateOf("")
    val carId: String
        get() = _carId.value


    fun onCarIdValueChange(value: String) {
        _carId.value = formatCarID(value)
        _carIdError.value = false
    }


    fun onCarIdTrailingIconClick() {
        _carId.value = ""
    }


    private fun formatCarID(value: String): String {
        val formattedValue = value.replace("[^a-zA-Z0-9]".toRegex(), "")

        return formattedValue
            .uppercase()
            .take(Settings.ACCIDENT_REPORT_CAR_ID_MAX_LEN)
            .replace("\\s+".toRegex(), " ")
    }


    /**
     * Forms - CATEGORY
     **/

    private val _selectedCategory = mutableStateOf(RoadAccidentCategory.OTHER)
    val selectedCategory: RoadAccidentCategory
        get() = _selectedCategory.value


    fun onCategorySelected(category: RoadAccidentCategory) {
        _selectedCategory.value = category
    }


    fun getAvailableCategories(): List<RoadAccidentCategory> {
        return RoadAccidentCategory.entries
    }


    fun categoryToString(category: RoadAccidentCategory): String {
        return category
            .toString()
            .replace("_", " ")
    }


    /**
     * Forms - DESCRIPTION
     **/

    private val _descriptionError = mutableStateOf(false)
    val descriptionError: Boolean
        get() = _descriptionError.value

    private val _description = mutableStateOf("")
    val description: String
        get() = _description.value


    fun onDescriptionValueChange(value: String) {
        _description.value = formatDescription(value)
        _descriptionError.value = false
    }


    private fun formatDescription(value: String): String {
        return value.take(ACCIDENT_REPORT_MAX_DESCRIPTION_LEN)
    }


    fun onDescriptionTrailingIconClick() {
        _description.value = ""
    }


    /**
     * Confirm Button
     **/


    fun onConfirmButtonClick() {
        if (!validateCondition())
            return


    }


    private fun validateCondition(): Boolean {
        if (carId.length < Settings.ACCIDENT_REPORT_CAR_ID_MIN_LEN) {
            _carIdError.value = true
            return false
        }

        if (description.length < Settings.ACCIDENT_REPORT_MIN_DESCRIPTION_LEN) {
            _descriptionError.value = true
            return false
        }

        return true
    }


    /**
     * Initializer
     **/


    init {
        setupCurrentLocation()
    }

}