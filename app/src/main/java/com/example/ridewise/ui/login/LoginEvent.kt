package com.example.ridewise.ui.login

sealed class LoginEvent {
    object StartOAuth : LoginEvent()
    object LoginSuccess : LoginEvent()
}