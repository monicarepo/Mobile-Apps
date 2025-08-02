package com.ms.trackify.authentication

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val googleIdOption: GetGoogleIdOption
) : AuthRepository {

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    private suspend fun handleCredential(credential: Credential): FirebaseUser {
        return when {
            credential is CustomCredential && credential.type == "https://accounts.google.com" -> {
                val googleIdToken = GoogleIdTokenCredential
                    .createFrom(credential.data)
                    .idToken
                    ?: throw Exception("Google ID token is null")

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                authResult.user ?: throw Exception("User not found after sign in")
            }
            else -> throw Exception("Unsupported credential type")
        }
    }

    override suspend fun signInWithGoogle(context: Context): AuthResult {
        return try {
            val result = credentialManager.getCredential(
                context = context,
                request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
            )
            val credential = result.credential
            Log.d("GoogleSignIn", "Credential received: ${result.credential.type}")

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                Log.d("GoogleSignIn", "Google ID Token: ${googleCredential.idToken}")
                val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                authResult.user?.let {
                    Log.d("GoogleSignIn", "User authenticated: ${it.uid}")
                    AuthResult.Success(it)
                } ?: run {
                    Log.e("GoogleSignIn", "No user after sign-in")
                    AuthResult.Error("Authentication failed")
                }
            } else {
                Log.e("GoogleSignIn", "Unexpected credential type")
                AuthResult.Error("Invalid credential type")
            }
        }
        catch (e: NoCredentialException) {
            Log.e("GoogleSignIn", "No Google credentials found: ${e.message}")
            return AuthResult.Error("No saved Google account found. Please try signing in manually.")
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error in Credential Manager: ${e.localizedMessage}")
            return AuthResult.Error("Google Sign-In failed: ${e.message}")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}