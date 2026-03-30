package com.example.ridewise.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Segment(
    val id: Long,
    val name: String,
    val distance: Double,
    @SerialName("elev_difference") val elevation_gain: Double? = null,
    val climb_category: Int? = null,
    val climb_category_desc: String? = null,
    val avg_grade: Double? = null,
    val start_latlng: List<Double>? = null,
    val end_latlng: List<Double>? = null,
    val points: String? = null,
    val starred: Boolean? = null
)