package com.ms.trackify.authentication

import android.app.Activity
import com.google.firebase.auth.FirebaseUser

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailError: Boolean = false,
    val emailErrorText: String = "",
    val isPasswordError: Boolean = false,
    val passwordErrorText: String = ""
)

interface AuthRepository {
    suspend fun signInWithGoogle(activity: Activity): AuthResult
    suspend fun signOut()
    fun getCurrentUser(): FirebaseUser?
}

sealed class AuthResult {
//    object Success : AuthResult()
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}