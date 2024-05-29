package com.pr.shoppinglist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pr.shoppinglist.data.LocationData

class LocationViewModel : ViewModel() {
    private val _location = mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> = _location

    fun updateLocation(newLocationData: LocationData) {
        _location.value = newLocationData
    }
}
