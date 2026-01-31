package com.peterchege.mobilewalletapp.core.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SendMoneyResponse(
    @SerialName("response_message")
    val responseMessage: String,

    @SerialName("response_status")
    val responseStatus: Boolean
)