package com.example.ridewise.data

import kotlinx.serialization.Serializable

@Serializable
data class AthleteStats(
    val biggest_ride_distance: Double = 0.0,
    val biggest_climb_elevation_gain: Double = 0.0,
    val recent_ride_totals: RideTotals = RideTotals()
)