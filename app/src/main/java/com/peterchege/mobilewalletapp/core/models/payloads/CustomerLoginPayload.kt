package com.peterchege.mobilewalletapp.core.models.payloads

import kotlinx.serialization.Serializable


@Serializable
data class CustomerLoginPayload(
    val customerId: String,
    val pin: String
)