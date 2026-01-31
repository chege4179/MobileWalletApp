package com.peterchege.mobilewalletapp.core.util

import com.peterchege.mobilewalletapp.core.models.responses.GenericErrorResponse

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()

    class Error<T : Any>(val error: GenericErrorResponse) : NetworkResult<T>()

    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}