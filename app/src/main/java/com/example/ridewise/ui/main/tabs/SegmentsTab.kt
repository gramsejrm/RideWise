package com.example.ridewise.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import com.example.ridewise.data.Segment
import com.example.ridewise.ktor.StravaRepository

@Composable
fun SegmentsTab(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { StravaRepository() }

    var segments by remember { mutableStateOf<List<Segment>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(latitude, longitude) {
        try {
            loading = true
            segments = repository.getSegmentsNearby(context, latitude, longitude)
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
        when {
            loading -> CircularProgressIndicator()
            error != null -> Text("Error: $error")
            segments.isEmpty() -> Text("No segments found nearby.")
            else -> LazyColumn {
                items(segments) { segment ->
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(segment.name, style = MaterialTheme.typography.titleMedium)
                        Text("Distance: ${"%.2f".format(segment.distance)} km")
                        Text("Elevation: ${"%.0f".format(segment.elevation_gain)} m")
                    }
                    Divider()
                }
            }
        }
    }
}