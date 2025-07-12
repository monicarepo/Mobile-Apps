package com.ms.trackify.authentication

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())

    fun onEmailChanged(value: String) {
        uiState = uiState.copy(
            email = value,
            isEmailError = value.isBlank() || !value.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")),
            emailErrorText = when {
                value.isBlank() -> "Email cannot be empty"
                !value.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) -> "Invalid email format"
                else -> ""
            }
        )
    }

    fun onPasswordChanged(value: String) {
        uiState = uiState.copy(
            password = value,
            isPasswordError = value.isBlank(),
            passwordErrorText = if (value.isBlank()) "Password cannot be empty" else ""
        )
    }

    fun signIn(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!uiState.isEmailError && !uiState.isPasswordError) {
            auth.signInWithEmailAndPassword(uiState.email, uiState.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }
}

