package com.udemy.weatherapp.models

import java.io.Serializable

data class Wind(
    val deg: Int,
    val speed: Double
): Serializable