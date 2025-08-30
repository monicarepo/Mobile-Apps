package com.ms.countryapp.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ms.countryapp.data.Country
import com.ms.countryapp.repository.ICountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryOperationViewModel @Inject constructor(private val countryRepository: ICountryRepository) : ViewModel(), CountryOperations {

    override val allCountries: MutableState<List<Country>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            fetchAndInsertAll()
        }
    }

    private suspend fun fetchAndInsertAll() {
        val job = viewModelScope.launch {
            countryRepository.fetchAndInsertAll()
        }
        job.join()
        getAllCountries()
    }

    override suspend fun getAllCountries() {
        allCountries.value = countryRepository.getAllCountries()
    }

    override suspend fun deleteCountry(country: Country) {
        country.let {
            countryRepository.deleteCountry(it)
        }
        getAllCountries()
    }

    override suspend fun updateCountry(
        country: Country,
        newCapital: String
    ) {
        country.let {
            countryRepository.updateCapital(it, newCapital)
        }
    }
}
