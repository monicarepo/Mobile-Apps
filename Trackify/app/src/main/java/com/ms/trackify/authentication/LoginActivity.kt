package com.ms.trackify.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ms.apptheme.ui.theme.AppTheme
import com.ms.trackify.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                LoginScreen(
                    onSignInSuccess = { navigateToHome() },
                    onNavigateToSignUp = { navigateToSignUp() }
                )
            }
        }
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}