package com.example.ridewise.data

import kotlinx.serialization.Serializable

@Serializable
data class SegmentResponse(
    val segments: List<Segment>
)