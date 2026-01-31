package com.peterchege.mobilewalletapp.ui.screens.transactions.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerBalancePayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.models.responses.RemoteTransaction
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RemoteTransactionScreenState(
    val transactions:List<RemoteTransaction> = emptyList(),
    val isLoading:Boolean = false,
    val errorMessage:String? = null,
)


@HiltViewModel
class RemoteTransactionScreenViewModel @Inject constructor(
    private val mobileWalletService: MobileWalletService,
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
) : ViewModel(){

    val userDetails = defaultUserDetailsProvider.userDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CustomerLoginResponse()
        )


    private val _screenState = MutableStateFlow(RemoteTransactionScreenState())
    val screenState = _screenState.asStateFlow()


    fun fetchLast100Transactions(
        customerId: String,
    ){
        viewModelScope.launch {
            _screenState.update { it.copy(isLoading = true) }
            val response = mobileWalletService.fetchLast100Transactions(
                payload = CustomerBalancePayload(
                    customerId = customerId
                )
            )
            _screenState.update {
                it.copy(
                    isLoading = false,
                )
            }

            when(response){
                is NetworkResult.Success -> {
                    _screenState.update {
                        it.copy(
                            transactions = response.data
                        )
                    }
                }
                is NetworkResult.Error -> {

                }
                else -> {}

            }
        }
    }
}