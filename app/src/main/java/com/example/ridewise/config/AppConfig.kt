package com.example.ridewise.config

import com.example.ridewise.BuildConfig

object AppConfig {
    val hasStravaCredentials: Boolean
        get() = BuildConfig.STRAVA_CLIENT_ID != "null" &&
                BuildConfig.STRAVA_CLIENT_SECRET != "null"
}