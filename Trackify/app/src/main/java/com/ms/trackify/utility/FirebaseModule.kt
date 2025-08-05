package com.ms.trackify.utility

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ms.trackify.Navigation.NavigationManager
import com.ms.trackify.R
import com.ms.trackify.authentication.AuthRepository
import com.ms.trackify.authentication.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideDatabase(): DatabaseReference {
        return Firebase.database.reference
    }

    @Provides
    fun provideGoogleIdOption(
        @ApplicationContext context: Context
    ): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
//            .setFilterByAuthorizedAccounts(true)
//            .setAutoSelectEnabled(true)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository {
        return impl
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideNavigationManager(): NavigationManager {
        return NavigationManager()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthProvidesModule {

    @Provides
    fun provideAuthRepositoryImpl(
        auth: FirebaseAuth,
        credentialManager: CredentialManager,
        googleIdOption: GetGoogleIdOption
    ): AuthRepositoryImpl {
        return AuthRepositoryImpl(auth, credentialManager, googleIdOption)
    }
}