package com.peterchege.mobilewalletapp.core.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class GenericErrorResponse(
    val timestamp: String,
    val message:String,
    val details:String? = null
)