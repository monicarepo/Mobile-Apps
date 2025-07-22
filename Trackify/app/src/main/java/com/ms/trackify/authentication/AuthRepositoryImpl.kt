package com.ms.trackify.authentication

import android.app.Activity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
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

    override suspend fun signInWithGoogle(activity: Activity): AuthResult {
        return try {
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response = credentialManager.getCredential(
                request = request,
                context = activity
            )

            val credential = response.credential
            val user = handleCredential(credential)
            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign in failed")
        }
    }

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

    override suspend fun signOut() {
        auth.signOut()
    }
}