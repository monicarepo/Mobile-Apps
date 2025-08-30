package com.ms.countryapp.database

interface DatabaseProvider {
    fun countryDao(): CountryDao
}