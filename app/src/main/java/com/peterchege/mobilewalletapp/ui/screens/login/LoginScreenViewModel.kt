package com.peterchege.mobilewalletapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerLoginPayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val customerId: String = "",
    val pin: String = "",
    val message: String? = null,
    val isLoading: Boolean = false
)

// ViewModel

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val apiService: MobileWalletService,
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
) : ViewModel() {
    private val _screenState = MutableStateFlow<LoginUiState>(LoginUiState())
    val screenState: StateFlow<LoginUiState> = _screenState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()


    fun onChangeCustomerId(value: String) {
        _screenState.update { it.copy(customerId = value) }
    }

    fun onPinChange(value: String) {
        _screenState.update { it.copy(pin = value) }
    }

    fun login(
        onLoginSuccess: (CustomerLoginResponse) -> Unit,
    ) {
        if (_screenState.value.customerId.isBlank() || _screenState.value.pin.isBlank()) {
            _snackbarMessage.value = "Please enter both Customer ID and PIN"
            return
        }

        if (_screenState.value.pin.length < 4) {
            _snackbarMessage.value = "PIN must be at least 4 digits"
            return
        }

        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }

            val response = apiService.customerLogin(
                payload = CustomerLoginPayload(
                    customerId = _screenState.value.customerId,
                    pin = _screenState.value.pin
                )
            )
            _screenState.update {
                it.copy(isLoading = false)
            }
            when (response) {
                is NetworkResult.Success -> {
                    defaultUserDetailsProvider.setUserDetails(details = response.data)
                    onLoginSuccess(response.data)
                }

                is NetworkResult.Error -> {
                    _snackbarMessage.update { response.error.message }
                }

                is NetworkResult.Exception -> {
                    _snackbarMessage.update { response.e.message }
                }
            }
        }
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}


