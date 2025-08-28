package com.ms.countryapp.viewModel

import androidx.compose.runtime.MutableState
import com.ms.countryapp.data.Country

interface CountryOperations {
    val allCountries: MutableState<List<Country>>
    suspend fun getAllCountries()
    suspend fun deleteCountry(country: Country)
    suspend fun  updateCountry(country: Country, newCapital: String)
}