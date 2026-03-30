package com.example.ridewise.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.ridewise.data.AuthTokens
import com.example.ridewise.ktor.StravaRepository
import com.example.ridewise.ui.login.LoginScreen
import com.example.ridewise.ui.login.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private val repository = StravaRepository() // repository handles token exchange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Debug: print intent data
        val data = intent?.data
        Log.d("LoginActivity", "Intent data: $data")

        val code = data?.getQueryParameter("code")
        Log.d("LoginActivity", "Authorization code: $code")

        // Extract the auth code from the redirect URI
        if (code != null) {
            lifecycleScope.launch {
                Log.d("LoginActivity", "Attempting to exchange tokens.")
                try {
                    val tokens: AuthTokens = repository.exchangeToken(code)
                    Log.d("LoginActivity", "Tokens: $tokens")
                    repository.saveTokens(this@LoginActivity, tokens)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Log.e("LoginActivity", e.toString())
                }
            }
        }

        setContent {
            MaterialTheme {
                val state = viewModel.uiState

                LoginScreen(
                    uiState = state,
                    onLoginClick = {
                        startStravaOAuth()
                    }
                )
            }
        }
    }

    private fun startStravaOAuth() {

        val authUri = "https://www.strava.com/oauth/authorize".toUri()
            .buildUpon()
            .appendQueryParameter("client_id", "215632")
            .appendQueryParameter("redirect_uri", "ridewise://callback")
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter("scope", "activity:write,read")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, authUri)
        startActivity(intent)
    }
}