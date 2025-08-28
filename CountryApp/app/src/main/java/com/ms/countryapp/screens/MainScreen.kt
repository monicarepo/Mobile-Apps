package com.ms.countryapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.ms.countryapp.CountryApp.Companion.APP_TAG
import com.ms.countryapp.components.CountryCard
import com.ms.countryapp.components.ObserveLoading
import com.ms.countryapp.dialogs.MyAlertDialog
import com.ms.countryapp.ui.theme.CountryAppTheme
import com.ms.countryapp.viewModel.CountryOperationViewModel
import com.ms.countryapp.viewModel.CountryViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(innerPaddingValues: PaddingValues,
               viewModel: CountryViewModel,
               countryOperationViewModel: CountryOperationViewModel) {

    val isLoading = remember { mutableStateOf(value = false) }

    val showDeleteAlertDialog = viewModel.showDeleteAlertDialog
    val showUpdateAlertDialog = viewModel.showUpdateAlertDialog
    val selectedCountryForDeletion = viewModel.selectedCountryForDeletion.value
    val selectedCountryForUpdation = viewModel.selectedCountryForUpdation.value

    //val countryList: MutableState<List<Country>> = Utility().readJsonFile(LocalContext.current)
    val countryList = countryOperationViewModel.allCountries.value
    Log.d(APP_TAG," countryList: $countryList")

    ObserveLoading( isLoading, countryOperationViewModel)

    CountryAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues),
            color = MaterialTheme.colorScheme.surface
        ) {
            when {
                isLoading.value -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else -> {
                    LazyColumn {
                        items(items = countryList) { country ->
                            CountryCard(country,showDeleteAlertDialog,showUpdateAlertDialog, viewModel)
                        }
//                        items(items = countryList.value, key = { country -> country.id ?: 0}) { country ->
//                            CountryCard(country,showDeleteAlertDialog,showUpdateAlertDialog, viewModel)
//                       }
                    }
                }
            }
        }
    }

    MyAlertDialog(showDialog = showUpdateAlertDialog,
        title = "Update Capital",
        message = "Enter new capital",
        currentCapital = selectedCountryForUpdation?.capital?.get(0) ?: "NA",
        positiveAction = { newCapital ->
            viewModel.viewModelScope.launch {
                selectedCountryForUpdation?.let {
                    //UpdateCountry
                }
            }
        }
    )

    MyAlertDialog(showDialog = showDeleteAlertDialog,
        title = "Delete Capital",
        message = "Do you want to delete this country?",
        currentCapital = selectedCountryForDeletion?.capital?.get(0) ?: "NA",
        positiveAction = {
            viewModel.viewModelScope.launch {
                selectedCountryForDeletion?.let {

                }
            }
        }
    )


}