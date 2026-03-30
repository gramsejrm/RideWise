package com.example.ridewise.data

import kotlinx.serialization.Serializable

@Serializable
data class AthleteProfile(
    val id: Long,
    val username: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val city: String? = null,
    val country: String? = null,
    val sex: String? = null,
    val weight: Double? = null
)