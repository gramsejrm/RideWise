package com.example.ridewise.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ridewise.config.AppConfig
import com.example.ridewise.ktor.StravaRepository
import com.example.ridewise.ui.main.MainScreen
import com.example.ridewise.ui.theme.RideWiseTheme

class MainActivity : ComponentActivity() {
    private val repository = StravaRepository() // repository handles token exchange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isLoggedIn = checkIfLoggedIn()

        if (!isLoggedIn) {
            // Navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContent {
            RideWiseTheme() {
                MainScreen()
            }
        }
    }

    private fun checkIfLoggedIn(): Boolean {
        if (!AppConfig.hasStravaCredentials) {
            Log.v("MainActivity", "No Credentials. Demo Mode is active")
            return true
        }
        try {
            repository.getAccessToken(this@MainActivity)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}