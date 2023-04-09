package com.vchat.presentation.navigation

/**
 * Created by Fasil on 11/03/23.
 */
sealed class NavRoute(val route: String) {
    object Splash : NavRoute("splash")
    object Login : NavRoute("login")
    object Register : NavRoute("register")
    object Home : NavRoute("home")
    object PasswordReset : NavRoute("password_reset")
    object AddEditPost : NavRoute("add_edit_post/{postID}")
    object ViewProfile : NavRoute("view_profile/{userId}")
    object EditProfile : NavRoute("edit_profile")
    object Settings : NavRoute("settings")
}
