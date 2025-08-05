package com.ms.trackify.Navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationManager @Inject constructor() : ViewModel() {
    private val _navigationEvents = Channel<NavigationEvent>(Channel.UNLIMITED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun navigateTo(route: String) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateTo(route))
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.NavigateUp)
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.ShowSnackbar(message))
        }
    }
}


sealed class NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent()
    object NavigateUp : NavigationEvent()
    data class ShowSnackbar(val message: String) : NavigationEvent()
}