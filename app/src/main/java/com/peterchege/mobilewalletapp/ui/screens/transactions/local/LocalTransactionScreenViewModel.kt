package com.peterchege.mobilewalletapp.ui.screens.transactions.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.database.dao.OfflineTransactionDao
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.SendMoneyPayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import com.peterchege.mobilewalletapp.core.util.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LocalTransactionScreenViewModel @Inject constructor(
    private val mobileWalletService: MobileWalletService,
    private val offlineTransactionDao: OfflineTransactionDao,
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
) : ViewModel() {

    val TAG = LocalTransactionScreenViewModel::class.java.simpleName

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    val userDetails = defaultUserDetailsProvider.userDetails
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CustomerLoginResponse()
        )

    val offlineTransactions = offlineTransactionDao.getAllOfflineTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearSnackbar(){
        _snackbarMessage.update { null }
    }

    fun syncTransaction(
        offlineTransaction: OfflineTransaction,
        customerLoginResponse: CustomerLoginResponse?
    ) {
        viewModelScope.launch {
            offlineTransactionDao.updateSyncStatusByTransactionId(
                id = offlineTransaction.clientTransactionId,
                syncStatus = SyncStatus.SYNCING.value
            )
            try {
                val response = mobileWalletService.sendMoney(
                    payload = SendMoneyPayload(
                        accountFrom = offlineTransaction.accountFrom,
                        accountTo = offlineTransaction.accountTo,
                        amount = offlineTransaction.amount.toInt(),
                        customerId = customerLoginResponse?.customerId ?: ""
                    )
                )
                when (response) {
                    is NetworkResult.Success -> {
                        if (response.data.responseStatus) {
                            offlineTransactionDao.updateSyncStatusByTransactionId(
                                id = offlineTransaction.clientTransactionId,
                                syncStatus = SyncStatus.SYNCED.value
                            )
                            _snackbarMessage.update { "Sync Successful" }
                        } else {
                            offlineTransactionDao.updateSyncStatusByTransactionId(
                                id = offlineTransaction.clientTransactionId,
                                syncStatus = SyncStatus.FAILED.value
                            )
                            _snackbarMessage.update { "Sync Failed" }
                        }
                    }

                    is NetworkResult.Error -> {
                        offlineTransactionDao.updateSyncStatusByTransactionId(
                            id = offlineTransaction.clientTransactionId,
                            syncStatus = SyncStatus.FAILED.value
                        )
                        _snackbarMessage.update { response.error.message }
                    }

                    is NetworkResult.Exception -> {
                        offlineTransactionDao.updateSyncStatusByTransactionId(
                            id = offlineTransaction.clientTransactionId,
                            syncStatus = SyncStatus.FAILED.value
                        )
                        _snackbarMessage.update { response.e.message }
                    }
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e)
                offlineTransactionDao.updateSyncStatusByTransactionId(
                    id = offlineTransaction.clientTransactionId,
                    syncStatus = SyncStatus.FAILED.value
                )
                _snackbarMessage.update { "An unexpected error occurred" }
            }

        }
    }


}

