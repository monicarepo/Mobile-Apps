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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ms.countryapp.CountryApp.Companion.APP_TAG
import com.ms.countryapp.composables.CountryCard
import com.ms.countryapp.data.Country
import com.ms.countryapp.ui.theme.CountryAppTheme
import com.ms.countryapp.util.Utility
import com.ms.countryapp.viewModel.CountryViewModel

@Composable
fun MainScreen(innerPaddingValues: PaddingValues, viewModel: CountryViewModel) {

    val isLoading = remember { mutableStateOf(value = false) }

    val showDeleteAlertDialog = viewModel.showDeleteAlertDialog
    val showUpdateAlertDialog = viewModel.showUpdateAlertDialog
    val selectedCountryForDeletion = viewModel.selectedCountryForDeletion
    val selectedCountryForUpdation = viewModel.selectedCountryForUpdation

    val countryList: MutableState<List<Country>> = Utility().readJsonFile(LocalContext.current)
//    countryList = Utility().readJsonFile()
    Log.d(APP_TAG," countryList: ${countryList.value[0].name}")

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
                        items(items = countryList.value) { country ->
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
}