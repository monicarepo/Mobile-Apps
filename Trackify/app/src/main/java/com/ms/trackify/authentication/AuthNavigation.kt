package com.ms.trackify.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()
    val authState = viewModel.authState

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(onSignInSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(onSignOut = viewModel::signOut)
        }
    }
}