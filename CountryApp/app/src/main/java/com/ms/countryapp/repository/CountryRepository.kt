package com.ms.countryapp.repository

import com.ms.countryapp.data.Country
import com.ms.countryapp.di.LocalProvider
import com.ms.countryapp.di.NetworkProvider
import com.ms.countryapp.repository.service.CountryListProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject


class CountryRepository @Inject constructor(
    @NetworkProvider private val countryListProvider: CountryListProvider,
    private val dispatcher: CoroutineDispatcher
): ICountryRepository {

    private var allCountries: List<Country> = emptyList()

    override suspend fun fetchAndInsertAll() = withContext(dispatcher) {
        val all_countries = getAllCountries()
        if(all_countries.isNotEmpty()){
            return@withContext
        }else {
            val countryList: List<Country> = countryListProvider.getCountryList()
            allCountries = countryList
            return@withContext
        }
    }

    override suspend fun getAllCountries(): List<Country> = withContext(dispatcher) {
        if(allCountries.isNotEmpty()){
            return@withContext allCountries
        }else {
            return@withContext allCountries
        }
    }

    override suspend fun deleteCountry(country: Country) = withContext(dispatcher){

    }

    override suspend fun updateCapital(
        country: Country,
        newCapital: String
    )  = withContext(dispatcher){
        val parsedString = "[\"${newCapital}\"]"
        val parsedArray = Json.decodeFromString<List<String>>(parsedString)
        val updatedCountry = country?.copy(capital = parsedArray)
        updatedCountry.let {
            allCountries = countryListProvider.getCountryList()
        }
    }

}