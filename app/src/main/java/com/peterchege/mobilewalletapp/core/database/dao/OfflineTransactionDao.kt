package com.peterchege.mobilewalletapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction
import com.peterchege.mobilewalletapp.core.util.SyncStatus
import kotlinx.coroutines.flow.Flow


@Dao
interface OfflineTransactionDao {

    @Query("SELECT * FROM offline_transactions ORDER BY createdAt DESC")
    fun getAllOfflineTransactions(): Flow<List<OfflineTransaction>>

    @Query(
        """
        UPDATE offline_transactions
        SET syncStatus = :syncStatus
        WHERE clientTransactionId = :id
    """
    )
    suspend fun updateSyncStatusByTransactionId(
        id: String, syncStatus: String
    ): Int

    @Query("SELECT * FROM offline_transactions WHERE syncStatus in (:syncStatus)")
    suspend fun getOfflineTransactionsBySyncStatus(syncStatus: List<String>): List<OfflineTransaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineTransaction(transaction: OfflineTransaction)
}