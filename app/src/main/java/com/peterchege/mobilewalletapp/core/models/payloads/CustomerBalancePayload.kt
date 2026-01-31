package com.peterchege.mobilewalletapp.core.models.payloads

import kotlinx.serialization.Serializable

@Serializable
data class CustomerBalancePayload(
    val customerId: String,
)