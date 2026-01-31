package com.peterchege.mobilewalletapp.core.util

import com.peterchege.mobilewalletapp.core.models.responses.GenericErrorResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import timber.log.Timber

suspend inline fun <reified T : Any> safeApiCall(
    json: Json,
    request: suspend () -> HttpResponse
): NetworkResult<T> {
    val TAG = "NetworkResult"
    return try {
        val response = request()

        when (response.status.value) {
            in 200..299 -> {
                // Deserialize the success response body to type T
                val responseBody = response.bodyAsText()
                val result = json.decodeFromString<T>(responseBody)
                NetworkResult.Success(result)
            }
            in 400..499 -> {
                // Deserialize the error response body to type E
                val errorBody = response.bodyAsText()
                val error = json.decodeFromString<GenericErrorResponse>(errorBody)
                NetworkResult.Error(error)
            }
            else -> {
                NetworkResult.Error(
                    error = GenericErrorResponse(
                        timestamp = "",
                        details = "An unexpected error occurred",
                        message = "An unexpected error occurred"
                    )
                )
            }
        }
    } catch (e: Exception) {
        Timber.tag(TAG).i("Network Error ${e.message}")
        Timber.tag(TAG).i("Network Error ${e.localizedMessage}")
        e.printStackTrace()
        NetworkResult.Error(
            error = GenericErrorResponse(
                timestamp = "",
                details = "An unexpected error occurred",
                message = "An unexpected error occurred"
            )
        )
    }
}