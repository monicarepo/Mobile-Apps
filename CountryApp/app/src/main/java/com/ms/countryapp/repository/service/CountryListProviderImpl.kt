package com.ms.countryapp.repository.service

import android.content.Context
import com.ms.countryapp.R
import com.ms.countryapp.data.Country
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CountryListProviderImpl @Inject constructor(private val context: Context) : CountryListProvider {

    private fun getJsonString(): String {
        val inputStream = context.resources.openRawResource(R.raw.countryinfo)
        return inputStream.bufferedReader().use { it.readText() }
    }

    override suspend fun getCountryList(): MutableList<Country> {
        val jsonStringFromRaw = getJsonString()
        return Json{ignoreUnknownKeys = true}.decodeFromString<MutableList<Country>>(jsonStringFromRaw)
    }

}