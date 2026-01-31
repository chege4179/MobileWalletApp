package com.peterchege.mobilewalletapp.core.models.responses

import kotlinx.serialization.Serializable


@Serializable
data class CustomerLoginResponse(
    val customerId: String? = null,
    val customerName: String? = null,
    val email: String? = null
)