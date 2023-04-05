package com.vchat.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun BottomNavBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomNavRoute.Chats,
        BottomNavRoute.Explore,
        BottomNavRoute.Profile
    )
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.White) {
        screens.forEach { bottomNavRoute ->
            AddItem(
                bottomNavRoute = bottomNavRoute,
                currentDestination = currentDestination,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    bottomNavRoute: BottomNavRoute,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    BottomNavigationItem(
        icon = {
            Icon(
                imageVector = bottomNavRoute.icon,
                contentDescription = null
            )
        },
        label = {
                Text(text = bottomNavRoute.title)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == bottomNavRoute.route
        } == true,
        onClick = {
            navHostController.navigate(bottomNavRoute.route) {
                popUpTo(navHostController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }, unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
    )
}