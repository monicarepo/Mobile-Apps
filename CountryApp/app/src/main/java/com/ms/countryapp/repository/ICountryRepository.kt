package com.ms.countryapp.repository

import com.ms.countryapp.data.Country

interface ICountryRepository {
    suspend fun fetchAndInsertAll()
    suspend fun getAllCountries(): List<Country>
    suspend fun deleteCountry(country: Country)
    suspend fun updateCapital(country: Country, newCapital: String)
}