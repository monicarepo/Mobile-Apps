package com.ms.countryapp.di

import com.ms.countryapp.repository.network.ApiService
import com.ms.countryapp.repository.service.CountryListProvider
import com.ms.countryapp.repository.service.CountryListProviderViaNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @NetworkProvider
    @Binds
    @Singleton
    abstract fun bindCountryListServiceProvider(
        countryListProviderViaNetwork: CountryListProviderViaNetwork
    ): CountryListProvider

    companion object {

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://restcountries.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}