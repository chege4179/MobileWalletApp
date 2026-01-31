package com.peterchege.mobilewalletapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerBalancePayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerBalanceResponse
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeScreenState(
    val customerBalance: CustomerBalanceResponse? = null,
    val snackbarMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
    private val mobileWalletService: MobileWalletService,
) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState = _screenState.asStateFlow()


    val userDetails = defaultUserDetailsProvider.userDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CustomerLoginResponse()
        )

    fun fetchAccountBalance(
        customerId: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            val response = mobileWalletService.fetchAccountBalanceByCustomerId(
                payload = CustomerBalancePayload(customerId = customerId)
            )
            _screenState.update { it.copy(isLoading = false) }

            when (response) {
                is NetworkResult.Success -> {
                    _screenState.update { it.copy(customerBalance = response.data) }
                    onSuccess()
                }

                is NetworkResult.Error -> {
                    _screenState.update {
                        it.copy(
                            snackbarMessage = response.error.message,
                            customerBalance = null
                        )

                    }

                }

                is NetworkResult.Exception -> {
                    _screenState.update {
                        it.copy(
                            snackbarMessage = response.e.message,
                            customerBalance = null
                        )

                    }
                }
            }

        }
    }

    fun onClickLogout(
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            defaultUserDetailsProvider.setUserDetails(details = CustomerLoginResponse())
            onSuccess()
        }

    }


    fun clearSnackbar() {
        _screenState.update { it.copy(snackbarMessage = null) }
    }


}