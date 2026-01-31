package com.peterchege.mobilewalletapp.core.models.payloads

import kotlinx.serialization.Serializable


@Serializable
data class SendMoneyPayload(
    val accountFrom: String,
    val accountTo: String,
    val amount: Int,
    val customerId: String
)