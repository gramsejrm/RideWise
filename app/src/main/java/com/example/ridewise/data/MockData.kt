package com.example.ridewise.data

object MockData {

    fun athleteProfile() = AthleteProfile(
        id = 123456,
        firstname = "Demo",
        lastname = "Rider"
    )

    fun athleteStats() = AthleteStats(
        recent_ride_totals = RideTotals(
            distance = 120_000.0,
            count = 8,
            elevation_gain = 1500.0,
            moving_time = 14400,
            elapsed_time = 15000,
            achievement_count = 5
        )
    )

    fun segments() = listOf(
        Segment(
            id = 1,
            name = "City Creek Climb",
            distance = 3200.0,
            elevation_gain = 250.0,
            avg_grade = 7.5
        ),
        Segment(
            id = 2,
            name = "Capitol Sprint",
            distance = 800.0,
            elevation_gain = 40.0,
            avg_grade = 5.2
        ),
        Segment(
            id = 3,
            name = "Emigration Canyon",
            distance = 10000.0,
            elevation_gain = 600.0,
            avg_grade = 6.0
        )
    )
}
