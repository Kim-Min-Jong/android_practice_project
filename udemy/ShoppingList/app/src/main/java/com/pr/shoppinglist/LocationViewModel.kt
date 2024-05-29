package com.pr.shoppinglist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pr.shoppinglist.data.GeocodingResult
import com.pr.shoppinglist.data.LocationData
import com.pr.shoppinglist.data.RetrofitClient
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf(listOf<GeocodingResult>())
    val address: State<List<GeocodingResult>> = _address

    fun updateLocation(newLocationData: LocationData) {
        _location.value = newLocationData
    }

    fun fetchAddress(
        latlng: String
    ) {
        try {
            viewModelScope.launch {
                val result = RetrofitClient.create().getAddressFromCoordinates(
                    latlng, ""
                )

                _address.value = result.results
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
