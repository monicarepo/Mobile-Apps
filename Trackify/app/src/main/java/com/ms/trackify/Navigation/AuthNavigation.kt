package com.ms.trackify.Navigation

import android.app.Dialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.ms.trackify.FaceDetection.FaceRegistrationScreen
import com.ms.trackify.authentication.AuthState
import com.ms.trackify.authentication.AuthViewModel
import com.ms.trackify.authentication.HomeScreen
import com.ms.trackify.authentication.LoginScreen
import com.ms.trackify.authentication.MyDialogScreen


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<AuthViewModel>()
    val authState = viewModel.authState

    val navigationManager: NavigationManager = viewModel.navigationManager

    LaunchedEffect(navigationManager) {
        Log.d("Navigation", "Received event1: ")
        navigationManager.navigationEvents.collect { event ->
            Log.d("Navigation", "Received event: $event")
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavigationEvent.NavigateUp -> navController.navigateUp()
                is NavigationEvent.ShowSnackbar -> {
                    // Show snackbar using scaffoldState
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(viewModel, onSignInSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("home") {
            viewModel.setCurrentUser("Monica!")
            HomeScreen(viewModel = viewModel, onSignOut = viewModel::signOut, addUser = viewModel::addUser)
        }

        // Normal screen
        composable("face_registration") {
            FaceRegistrationScreen(
                onRegistrationComplete = { faceUri ->
                    // Handle registration completion
                    navController.popBackStack()
                }
            )
        }

//        // define route with argument
//        composable("details/{userId}") { backStackEntry ->
//            val userId = backStackEntry.arguments?.getString("userId")
//            DetailScreen(userId)
//        }

        // Dialog screen
        dialog("myDialog") {
            MyDialogScreen(navController)
        }
    }
}
