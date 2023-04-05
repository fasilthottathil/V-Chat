package com.vchat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vchat.presentation.home.Home
import com.vchat.presentation.login.Login
import com.vchat.presentation.login.LoginViewModel
import com.vchat.presentation.passwordReset.ResetPassword
import com.vchat.presentation.passwordReset.ResetPasswordViewModel
import com.vchat.presentation.register.Register
import com.vchat.presentation.register.RegisterViewModel
import com.vchat.presentation.splash.Splash
import com.vchat.presentation.splash.SplashViewModel
import com.vchat.utils.popUpTo

/**
 * Created by Fasil on 11/03/23.
 */
@Composable
fun AppNavigation(
    navHostController: NavHostController,
    startDestination: String = NavRoute.Splash.route
) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(NavRoute.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            Splash {
                if (splashViewModel.isLogin()) {
                    navHostController.navigate(
                        NavRoute.Home.route, navOptions = popUpTo(
                            NavRoute.Splash.route,
                            true
                        )
                    )
                } else {
                    navHostController.navigate(
                        NavRoute.Login.route, navOptions = popUpTo(
                            NavRoute.Splash.route,
                            true
                        )
                    )
                }
            }
        }
        composable(NavRoute.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            Login(
                error = loginViewModel.error,
                loading = loginViewModel.loading,
                onLogin = loginViewModel.onLogin,
                onClickLogin = { email, password ->
                    loginViewModel.validateInputFields(email, password)
                }, resetError = {
                    loginViewModel.resetError()
                }, onClickRegister = {
                    navHostController.navigate(NavRoute.Register.route)
                }, onClickForgotPassword = {
                    navHostController.navigate(NavRoute.PasswordReset.route)
                }, navigateToHome = {
                    navHostController.navigate(
                        NavRoute.Home.route,
                        navOptions = popUpTo(NavRoute.Login.route, true)
                    )
                }
            )
        }
        composable(NavRoute.Register.route) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            Register(
                error = registerViewModel.error,
                loading = registerViewModel.loading,
                onRegistered = registerViewModel.onRegistered,
                onClickRegister = { username, email, password, gender ->
                    registerViewModel.validateInputFields(username, email, password, gender)
                },
                resetError = {
                    registerViewModel.resetError()
                }, onClickLogin = {
                    navHostController.navigateUp()
                }, navigateToHome = {
                    navHostController.navigate(
                        NavRoute.Home.route,
                        navOptions = popUpTo(NavRoute.Login.route, true)
                    )
                }
            )
        }
        composable(NavRoute.PasswordReset.route) {
            val resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
            ResetPassword(
                error = resetPasswordViewModel.error,
                loading = resetPasswordViewModel.loading,
                onResetEmailSend = resetPasswordViewModel.onResetEmailSend,
                onClickReset = { resetPasswordViewModel.validateInputFields(it) },
                resetError = { resetPasswordViewModel.resetError() }) {
                navHostController.navigateUp()
            }
        }
        composable(NavRoute.Home.route) {
            Home()
        }
    }
}