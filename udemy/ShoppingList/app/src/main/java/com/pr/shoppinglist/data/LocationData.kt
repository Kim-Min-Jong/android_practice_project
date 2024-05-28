package com.pr.shoppinglist.data

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

data class GeocodingResponse(
    val results: List<LocationData>,
    val status: String
)

data class GeocodingResult(
    val formattedAddress: String
)
