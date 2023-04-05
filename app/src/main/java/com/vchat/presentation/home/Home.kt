package com.vchat.presentation.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vchat.presentation.navigation.BottomNavBar
import com.vchat.presentation.navigation.BottomNavRoute
import com.vchat.presentation.navigation.MainNavGraph

/**
 * Created by Fasil on 18/03/23.
 */
@Composable
fun Home() {
    val navHostController = rememberNavController()
    val bottomVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    bottomVisibility.value = when (navBackStackEntry?.destination?.route) {
        BottomNavRoute.Chats.route -> true
        BottomNavRoute.Explore.route -> true
        BottomNavRoute.Profile.route -> true
        else -> false
    }
    Scaffold(bottomBar = { if (bottomVisibility.value) {
        BottomNavBar(navHostController = navHostController)
    } }) {
        MainNavGraph(navHostController = navHostController, modifier = Modifier.padding(it))
    }
}
