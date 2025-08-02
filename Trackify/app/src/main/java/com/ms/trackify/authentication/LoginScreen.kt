package com.ms.trackify.authentication


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ms.trackify.R

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {}
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val authState = viewModel.authState

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onSignInSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign In", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            isError = state.isEmailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.isEmailError) {
            Text(text = state.emailErrorText, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            isError = state.isPasswordError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.isPasswordError) {
            Text(text = state.passwordErrorText, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                viewModel.signIn(
                    context,
                    onSuccess = onSignInSuccess,
                    onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Sign In", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "New here? Create an account",
            color = Color.Blue,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onNavigateToSignUp() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        signInButton(
            text = "Sign in with Google",
            loadingText = "Signing in...",
            isLoading = false,
            icon = painterResource(id = R.drawable.google_logo),
            onClick = {
                viewModel.signInWithGoogle(context)
            }
        )
    }
}