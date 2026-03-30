package com.example.ridewise.ui.main

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ridewise.config.AppConfig
import com.example.ridewise.data.AthleteProfile
import com.example.ridewise.ktor.StravaRepository
import com.example.ridewise.ui.LoginActivity
import com.example.ridewise.ui.main.tabs.BottomNavScreen
import com.example.ridewise.ui.tabs.ProfileTab
import com.example.ridewise.ui.tabs.SegmentsTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val repository = remember { StravaRepository() }
    val context = LocalContext.current
    var athleteProfile: AthleteProfile? = null

    var selectedTab by remember { mutableStateOf<BottomNavScreen>(BottomNavScreen.Profile) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            loading = true
            athleteProfile = repository.getCurrentAthlete(context)
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RideWise") },
                actions = {
                    IconButton(onClick = {
                        if (!AppConfig.hasStravaCredentials) {
                            Toast.makeText(
                                context,
                                "Cannot log out in demo mode",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@IconButton
                        }
                        repository.clearTokens(context)
                        context.startActivity(
                            Intent(context, LoginActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                listOf(
                    BottomNavScreen.Profile,
                    BottomNavScreen.Segments,
                ).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedTab == screen,
                        onClick = { selectedTab = screen }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                loading -> CircularProgressIndicator()
                error != null -> Text("Error loading athlete: $error")
                athleteProfile != null ->
                    when (selectedTab) {
                        BottomNavScreen.Profile -> ProfileTab(
                            athlete = athleteProfile,
                            modifier = Modifier.padding(innerPadding)
                        )

                        BottomNavScreen.Segments -> {
                            SegmentsTab(
                                latitude = 51.2976,
                                longitude = -111.8583,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
            }
        }
    }
}