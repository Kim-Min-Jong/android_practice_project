package com.udemy.weatherapp.models

import java.io.Serializable

data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Long,
    val sunset: Long,
    val type: Int
): Serializable