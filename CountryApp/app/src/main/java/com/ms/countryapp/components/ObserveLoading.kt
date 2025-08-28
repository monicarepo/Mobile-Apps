package com.ms.countryapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.ms.countryapp.viewModel.CountryOperationViewModel

@Composable
fun ObserveLoading(isLoading: MutableState<Boolean>, viewModel: CountryOperationViewModel) {
    val allCountries = viewModel.allCountries.value

    LaunchedEffect(allCountries) {
        isLoading.value = allCountries.isEmpty()
    }
}