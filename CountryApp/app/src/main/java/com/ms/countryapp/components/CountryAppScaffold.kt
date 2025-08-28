package com.ms.countryapp.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ms.countryapp.R
import com.ms.countryapp.screens.MainScreen
import com.ms.countryapp.viewModel.CountryOperationViewModel
import com.ms.countryapp.viewModel.CountryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAppScaffold() {
    val viewModel = hiltViewModel<CountryViewModel>()
    val countryOperationViewModel = hiltViewModel<CountryOperationViewModel>()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.labelLarge)
                },
                actions = {
                    IconButton(onClick = {
                        //Handle Buton
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search))
                    }
                    IconButton(onClick = {/* Handle click */}) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        MainScreen(innerPadding, viewModel, countryOperationViewModel)
    }
}
