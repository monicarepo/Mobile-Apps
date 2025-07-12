package com.ms.trackify.authentication

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailError: Boolean = false,
    val emailErrorText: String = "",
    val isPasswordError: Boolean = false,
    val passwordErrorText: String = ""
)