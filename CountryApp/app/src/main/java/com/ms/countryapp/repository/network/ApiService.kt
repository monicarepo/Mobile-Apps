package com.ms.countryapp.repository.network

import com.ms.countryapp.data.Country
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v3.1/all?fields=name,flags,capital,region,subregion,currencies,entries,idd,tld")
    suspend fun getAllCountries(): Response<MutableList<Country>>
}