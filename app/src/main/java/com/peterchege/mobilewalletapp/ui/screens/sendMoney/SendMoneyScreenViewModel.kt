package com.peterchege.mobilewalletapp.ui.screens.sendMoney

import androidx.compose.animation.core.updateTransition
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.database.dao.OfflineTransactionDao
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerBalancePayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerBalanceResponse
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import com.peterchege.mobilewalletapp.core.util.SyncStatus
import com.peterchege.mobilewalletapp.core.work.SyncTransactionsWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


data class SendMoneyScreenState(
    val creditAccountNumber: String = "",
    val amount: String = "",
    val snackbarMessage: String? = null,
    val customerBalance: CustomerBalanceResponse? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class SendMoneyScreenViewModel @Inject constructor(
    private val mobileWalletService: MobileWalletService,
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
    private val offlineTransactionDao: OfflineTransactionDao,
    private val syncTransactionsWorkManager: SyncTransactionsWorkManager,
) : ViewModel() {

    private val _screenState = MutableStateFlow(SendMoneyScreenState())
    val screenState = _screenState.asStateFlow()


    val userDetails = defaultUserDetailsProvider.userDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CustomerLoginResponse()
        )

    val isSyncing = syncTransactionsWorkManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )


    fun onChangeCreditAccountNumber(value: String) {
        _screenState.update { it.copy(creditAccountNumber = value) }
    }

    fun onChangeAmount(value: String) {
        if (value.all { it.isDigit() }) {
            _screenState.update {
                it.copy(amount = value)
            }
        }

    }

    fun clearSnackbar() {
        _screenState.update { it.copy(snackbarMessage = null) }
    }

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

    fun onSubmit(
        customerBalanceResponse: CustomerBalanceResponse,
    ) {
        viewModelScope.launch {
            val offlineTransaction = OfflineTransaction(
                clientTransactionId = UUID.randomUUID().toString(),
                accountFrom = customerBalanceResponse.accountNo,
                accountTo = _screenState.value.creditAccountNumber,
                amount = _screenState.value.amount,
                syncStatus = SyncStatus.QUEUED.value,
            )
            offlineTransactionDao.insertOfflineTransaction(transaction = offlineTransaction)
            _screenState.update {
                it.copy(
                    creditAccountNumber = "",
                    amount = "",
                    snackbarMessage = "Transaction queued for syncing...."
                )
            }
            syncTransactionsWorkManager.startSync()
        }
    }
}