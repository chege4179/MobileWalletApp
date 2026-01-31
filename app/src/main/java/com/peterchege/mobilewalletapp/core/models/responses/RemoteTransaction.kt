package com.peterchege.mobilewalletapp.core.models.responses

import kotlinx.serialization.Serializable


@Serializable
data class RemoteTransaction(
    val accountNo: String,
    val amount: Double,
    val balance: Double,
    val customerId: String,
    val debitOrCredit: String,
    val id: Int,
    val transactionId: String,
    val transactionType: String
)