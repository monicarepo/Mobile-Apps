package com.ms.countryapp.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject() constructor(): ViewModel() {
    val showDeleteAlertDialog = mutableStateOf(false)
    val showUpdateAlertDialog = mutableStateOf(false)

    var selectedCountryForDeletion = mutableStateOf(null)
    var selectedCountryForUpdation = mutableStateOf(null)

}