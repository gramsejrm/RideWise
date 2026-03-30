package com.example.ridewise.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // UI State
    var uiState by mutableStateOf(LoginUiState())
        private set

    // One-time events (navigation, launching OAuth, etc.)
    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    fun onLoginClicked() {
        viewModelScope.launch {
            _events.emit(LoginEvent.StartOAuth)
        }
    }

    fun onAuthCodeReceived(code: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            try {
                // TODO: Replace with real repository call
                delay(1000)

                uiState = uiState.copy(
                    isLoading = false,
                    isLoggedIn = true
                )

                _events.emit(LoginEvent.LoginSuccess)

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Login failed"
                )
            }
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}