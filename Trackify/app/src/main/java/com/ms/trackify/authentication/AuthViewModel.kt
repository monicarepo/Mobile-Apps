package com.ms.trackify.authentication

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
    private val database: DatabaseReference
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
    private var _authState by mutableStateOf<AuthState>(AuthState.Idle)
    val authState: AuthState get() = _authState

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        authRepository.getCurrentUser()?.let { user ->
            _authState = AuthState.Authenticated(user)
        }
    }

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

    fun signInWithGoogle(activity: Activity) {
        _authState = AuthState.Loading
        viewModelScope.launch {
            _authState = when (val result = authRepository.signInWithGoogle(activity)) {
                is AuthResult.Success -> {
                    AuthState.Authenticated(result.user)
                }

                is AuthResult.Error -> {
                    AuthState.Error(result.message)
                }

                AuthResult.Loading -> {
                    AuthState.Loading
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState = AuthState.Idle
        }
    }
}

