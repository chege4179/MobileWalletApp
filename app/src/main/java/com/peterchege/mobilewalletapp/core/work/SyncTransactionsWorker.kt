package com.peterchege.mobilewalletapp.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.database.dao.OfflineTransactionDao
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.models.payloads.SendMoneyPayload
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import com.peterchege.mobilewalletapp.core.util.SyncStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber


@HiltWorker
class SyncTransactionsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParameters: WorkerParameters,

    private val mobileWalletService: MobileWalletService,
    private val offlineTransactionDao: OfflineTransactionDao,
    private val defaultUserDetailsProvider: DefaultUserDetailsProvider,
) :CoroutineWorker(appContext,workerParameters){
    val TAG = SyncTransactionsWorker::class.java.simpleName

    override suspend fun doWork(): Result {
        try {
            Timber.tag(TAG).i("Starting to sync queued transactions")
            val userDetails = defaultUserDetailsProvider.userDetails.first()
            val queuedTransactions = offlineTransactionDao.getOfflineTransactionsBySyncStatus(
                syncStatus = listOf(
                    SyncStatus.QUEUED.value,
                    SyncStatus.FAILED.value,
                )
            )
            Timber.tag(TAG).i("Number of queued Transactions ${queuedTransactions.size}")
            if (queuedTransactions.isEmpty()){
                return Result.success()
            }else{
                queuedTransactions.forEach { queuedTransaction ->
                    offlineTransactionDao.updateSyncStatusByTransactionId(
                        id = queuedTransaction.clientTransactionId,
                        syncStatus = SyncStatus.SYNCING.value
                    )
                    try {
                        val response = mobileWalletService.sendMoney(
                            payload = SendMoneyPayload(
                                accountFrom = queuedTransaction.accountFrom,
                                accountTo = queuedTransaction.accountTo,
                                amount = queuedTransaction.amount.toInt(),
                                customerId = userDetails.customerId ?:""
                            )
                        )
                        when(response){
                            is NetworkResult.Success -> {
                                if (response.data.responseStatus){
                                    offlineTransactionDao.updateSyncStatusByTransactionId(
                                        id = queuedTransaction.clientTransactionId,
                                        syncStatus = SyncStatus.SYNCED.value
                                    )
                                }else{
                                    offlineTransactionDao.updateSyncStatusByTransactionId(
                                        id = queuedTransaction.clientTransactionId,
                                        syncStatus = SyncStatus.FAILED.value
                                    )
                                }
                            }
                            is NetworkResult.Error -> {
                                offlineTransactionDao.updateSyncStatusByTransactionId(
                                    id = queuedTransaction.clientTransactionId,
                                    syncStatus = SyncStatus.FAILED.value
                                )
                            }
                            is NetworkResult.Exception -> {
                                offlineTransactionDao.updateSyncStatusByTransactionId(
                                    id = queuedTransaction.clientTransactionId,
                                    syncStatus = SyncStatus.FAILED.value
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Timber.tag(TAG).e(e)
                        offlineTransactionDao.updateSyncStatusByTransactionId(
                            id = queuedTransaction.clientTransactionId,
                            syncStatus = SyncStatus.FAILED.value
                        )
                    }
                }
            }
        }catch (e:Exception){
            Timber.tag(TAG).e(e)
            return Result.failure()
        }

        return Result.success()
    }
}