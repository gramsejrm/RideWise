package com.example.ridewise.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ridewise.data.AthleteProfile
import com.example.ridewise.data.AthleteStats
import com.example.ridewise.ktor.StravaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProfileTab(
    athlete: AthleteProfile,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { StravaRepository() }

    var stats by remember { mutableStateOf<AthleteStats?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch stats when tab is displayed
    LaunchedEffect(Unit) {
        try {
            loading = true
            stats = withContext(Dispatchers.IO) {
                repository.getAthleteStats(context, athlete.id)
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Athlete name at the top
        Text(
            text = "${athlete.firstname.orEmpty()} ${athlete.lastname.orEmpty()}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text("Error: $error")
            stats != null -> {
                val ride = stats!!.recent_ride_totals
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Recent Rides: ${ride.count}")
                    Text("Total Distance: ${"%.2f".format(ride.distance)} km")
                    Text("Elevation Gain: ${"%.0f".format(ride.elevation_gain)} m")
                    Text("Achievements: ${ride.achievement_count}")
                    Text("Biggest Ride Distance: ${"%.2f".format(stats!!.biggest_ride_distance)} km")
                    Text("Biggest Climb Elevation Gain: ${"%.0f".format(stats!!.biggest_climb_elevation_gain)} m")
                }
            }
        }
    }
}