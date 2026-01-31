package com.peterchege.mobilewalletapp.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.mobilewalletapp.core.database.dao.OfflineTransactionDao
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction


@Database(
    entities = [
        OfflineTransaction::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class MobileWalletAppDatabase : RoomDatabase() {


    abstract val offlineTransactionDao: OfflineTransactionDao



}