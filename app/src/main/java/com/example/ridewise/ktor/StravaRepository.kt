package com.example.ridewise.ktor

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.ridewise.BuildConfig
import com.example.ridewise.config.AppConfig
import com.example.ridewise.data.AthleteProfile
import com.example.ridewise.data.AthleteStats
import com.example.ridewise.data.AuthTokens
import com.example.ridewise.data.MockData
import com.example.ridewise.data.Segment
import com.example.ridewise.data.SegmentResponse
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StravaRepository {

    /**
     * Retrieves the stored Strava access token from EncryptedSharedPreferences.
     * @throws Exception if no token is found or it has expired.
     */
    fun getAccessToken(context: Context): String {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            context,
            "strava_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val token = prefs.getString("access_token", null)
        val expiresAt = prefs.getLong("expires_at", 0L)
        val currentTimeSec = System.currentTimeMillis() / 1000

        if (token.isNullOrEmpty() || currentTimeSec >= expiresAt) {
            throw Exception("Access token missing or expired")
        }

        return token
    }

    fun saveTokens(context: Context, tokens: AuthTokens) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            context,
            "strava_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        prefs.edit {
            putString("access_token", tokens.accessToken)
                .putString("refresh_token", tokens.refreshToken)
                .putLong("expires_at", tokens.expiresAt)
        }
    }

    fun clearTokens(context: Context) {
        // Clear Strava tokens
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            context,
            "strava_tokens",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        prefs.edit { clear() }
    }

    suspend fun getCurrentAthlete(context: Context): AthleteProfile = withContext(Dispatchers.IO) {
        if (!AppConfig.hasStravaCredentials) {
            MockData.athleteProfile()
        } else {
            val token = getAccessToken(context) // reuse the token retrieval function

            stravaClient.get("https://www.strava.com/api/v3/athlete") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        }
    }

    suspend fun getAthleteStats(context: Context, athleteId: Long): AthleteStats =
        withContext(Dispatchers.IO) {
            if (!AppConfig.hasStravaCredentials) {
                MockData.athleteStats()
            } else {
                val token = getAccessToken(context)

                stravaClient.get("https://www.strava.com/api/v3/athletes/$athleteId/stats") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }.body()
            }
        }

    suspend fun getSegmentsNearby(
        context: Context,
        lat: Double,
        lng: Double
    ): List<Segment> = withContext(Dispatchers.IO) {
        if (!AppConfig.hasStravaCredentials) {
            MockData.segments()
        } else {
            val token = getAccessToken(context)
            // 100 km bounding box
            val latDelta = 0.9   // ~100 km
            val lngDelta = 1.18  // ~100 km at 40°N

            val bounds = "${lat - latDelta},${lng - lngDelta},${lat + latDelta},${lng + lngDelta}"
            stravaClient.get(
                "https://www.strava.com/api/v3/segments/explore?bounds=$bounds&activity_type=ride"
            ) {
                header("Authorization", "Bearer $token")
            }.body<SegmentResponse>().segments
        }
    }

    suspend fun exchangeToken(code: String): AuthTokens = withContext(Dispatchers.IO) {
        stravaClient.submitForm(
            url = "https://www.strava.com/oauth/token",
            formParameters = Parameters.Companion.build {
                append("client_id", BuildConfig.STRAVA_CLIENT_ID)
                append("client_secret", BuildConfig.STRAVA_CLIENT_SECRET)
                append("code", code)
                append("grant_type", "authorization_code")
            },
            encodeInQuery = false // ensures it POSTs in body, not in URL
        ).body()
    }

    suspend fun refreshToken(refreshToken: String): AuthTokens = withContext(Dispatchers.IO) {
        stravaClient.submitForm(
            url = "https://www.strava.com/oauth/token",
            formParameters = Parameters.Companion.build {
                append("client_id", BuildConfig.STRAVA_CLIENT_ID)
                append("client_secret", BuildConfig.STRAVA_CLIENT_SECRET)
                append("refresh_token", refreshToken)
                append("grant_type", "refresh_token")
            }
        ).body()
    }
}