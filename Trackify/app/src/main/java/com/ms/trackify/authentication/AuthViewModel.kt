package com.ms.trackify.authentication

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.ms.trackify.Navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
    private val database: DatabaseReference,
    val navigationManager: NavigationManager
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
    private var _authState by mutableStateOf<AuthState>(AuthState.Idle)
    val authState: AuthState get() = _authState

    internal var stateVersion by mutableIntStateOf(0)

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        authRepository.getCurrentUser()?.let { user ->
            _authState = AuthState.Authenticated(user)
        }
    }

    private fun updateAuthState(newState: AuthState) {
        _authState = newState
        stateVersion++ // This forces recomposition
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

    fun signInWithGoogle(context: Context) {
        updateAuthState(AuthState.Loading)
        viewModelScope.launch {
            updateAuthState(
                when (val result = authRepository.signInWithGoogle(context)) {
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
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                Log.d("signOut","Before signOut: ${_authState}")
                authRepository.signOut()
                updateAuthState(AuthState.Idle)
                Log.d("signOut","After signOut: ${_authState}")
            } catch (e: Exception) {
                Log.d("signOut","Exception signOut: ${e.message}")
                updateAuthState(AuthState.Error("Sign out failed: ${e.message}"))
            }
        }
    }

    fun addUser() {
        viewModelScope.launch {
            navigationManager.navigateTo("face_registration")
        }
    }

}

