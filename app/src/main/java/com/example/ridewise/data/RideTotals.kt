package com.example.ridewise.data

import kotlinx.serialization.Serializable

@Serializable
data class RideTotals(
    val distance: Double = 0.0,       // in kilometers
    val achievement_count: Int = 0,
    val count: Int = 0,
    val elapsed_time: Int = 0,        // seconds
    val elevation_gain: Double = 0.0, // meters
    val moving_time: Int = 0           // seconds
)