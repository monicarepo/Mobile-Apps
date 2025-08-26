package com.ms.countryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CountryApp: Application() {
    companion object {
        val APP_TAG = "CountryAppTag"
    }
}