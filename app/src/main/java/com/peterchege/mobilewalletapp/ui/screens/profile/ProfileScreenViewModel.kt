package com.peterchege.mobilewalletapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerLoginPayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import com.peterchege.mobilewalletapp.ui.screens.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
) : ViewModel() {

    val userDetails = defaultUserDetailsProvider.userDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CustomerLoginResponse()
        )

}
