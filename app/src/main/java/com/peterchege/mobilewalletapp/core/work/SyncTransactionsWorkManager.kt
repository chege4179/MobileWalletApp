package com.peterchege.mobilewalletapp.core.work

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.peterchege.mobilewalletapp.core.util.Constants.syncTransactionsWorker
import com.peterchege.mobilewalletapp.core.util.anyRunning
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate

interface SyncTransactionsWorkManager {
    val isSyncing: Flow<Boolean>

    suspend fun startSync()
}


class SyncTransactionsWorkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : SyncTransactionsWorkManager {


    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(syncTransactionsWorker)
            .map(List<WorkInfo>::anyRunning)
            .asFlow()
            .conflate()

    override suspend fun startSync() {
        val syncTransactionsRequest = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setInputData(SyncTransactionsWorker::class.delegatedData())
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(
            syncTransactionsWorker,
            ExistingWorkPolicy.KEEP,
            syncTransactionsRequest
        )
            .enqueue()
    }

}