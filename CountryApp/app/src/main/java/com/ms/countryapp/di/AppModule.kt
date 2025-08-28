package com.ms.countryapp.di

import android.app.Application
import android.content.Context
import com.ms.countryapp.repository.CountryRepository
import com.ms.countryapp.repository.ICountryRepository
import com.ms.countryapp.repository.service.CountryListProvider
import com.ms.countryapp.repository.service.CountryListProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideCountryRepository(
        @NetworkProvider countryListProvider: CountryListProvider,
        dispatcher: CoroutineDispatcher
    ): ICountryRepository {
        return CountryRepository(countryListProvider,dispatcher)
    }

    @LocalProvider
    @Provides
    @Singleton
    fun provideCountryListServiceProviderImpl(context: Context): CountryListProvider {
        return CountryListProviderImpl(context)
    }
}