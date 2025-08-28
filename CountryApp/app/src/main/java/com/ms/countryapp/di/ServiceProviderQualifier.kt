package com.ms.countryapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NetworkProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalProvider