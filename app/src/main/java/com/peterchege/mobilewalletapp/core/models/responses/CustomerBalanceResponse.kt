package com.peterchege.mobilewalletapp.core.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class CustomerBalanceResponse(
    val balance: String,
    val accountNo: String
)