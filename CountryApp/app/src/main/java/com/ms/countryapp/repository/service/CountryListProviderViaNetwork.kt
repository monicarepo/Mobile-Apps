package com.ms.countryapp.repository.service

import com.ms.countryapp.data.Country
import com.ms.countryapp.repository.network.ApiService
import javax.inject.Inject

class CountryListProviderViaNetwork  @Inject constructor(
    private val apiService: ApiService
): CountryListProvider{

    override suspend fun getCountryList(): MutableList<Country> {
       return apiService.getAllCountries().body()!!
    }

}