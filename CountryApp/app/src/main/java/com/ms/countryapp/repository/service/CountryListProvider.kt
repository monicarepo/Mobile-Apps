package com.ms.countryapp.repository.service

import com.ms.countryapp.data.Country

interface CountryListProvider {
    suspend fun getCountryList(): MutableList<Country>
}