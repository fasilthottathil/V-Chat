package com.vchat.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by Fasil on 19/03/23.
 */
sealed class BottomNavRoute(val route: String, val title: String, val icon: ImageVector) {
    object Chats : BottomNavRoute("chats", "Chats", Icons.Default.Chat)
    object Explore : BottomNavRoute("explore", "Explore", Icons.Default.Explore)
    object Profile : BottomNavRoute("profile", "Profile", Icons.Default.Person)
}
