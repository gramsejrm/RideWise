package com.example.ridewise.ui.main.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person

sealed class BottomNavScreen(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Profile : BottomNavScreen("Profile", Icons.Default.Person)
    object Segments : BottomNavScreen("Segments", Icons.Default.DateRange)
}
