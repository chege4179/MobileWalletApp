package com.peterchege.mobilewalletapp.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "offline_transactions")
data class OfflineTransaction(
    @PrimaryKey
    val clientTransactionId: String,
    val accountFrom: String,
    val accountTo: String,
    val amount: String,
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String,
)
