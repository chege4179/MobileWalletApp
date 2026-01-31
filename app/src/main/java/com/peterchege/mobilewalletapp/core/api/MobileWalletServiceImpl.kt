package com.peterchege.mobilewalletapp.core.api

import com.peterchege.mobilewalletapp.core.models.payloads.CustomerBalancePayload
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerLoginPayload
import com.peterchege.mobilewalletapp.core.models.payloads.SendMoneyPayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerBalanceResponse
import io.ktor.client.HttpClient
import javax.inject.Inject
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.models.responses.SendMoneyResponse
import com.peterchege.mobilewalletapp.core.util.Constants.prefix
import com.peterchege.mobilewalletapp.core.util.NetworkResult
import com.peterchege.mobilewalletapp.core.util.safeApiCall
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.json.Json

class MobileWalletServiceImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) : MobileWalletService {

    override suspend fun customerLogin(payload: CustomerLoginPayload): NetworkResult<CustomerLoginResponse> {
        return safeApiCall<CustomerLoginResponse>(
            json = json,
            request = {
                httpClient.post("$prefix/api/v1/customers/login"){
                    setBody(payload)
                }
            }
        )
    }

    override suspend fun fetchAccountBalanceByCustomerId(payload: CustomerBalancePayload): NetworkResult<CustomerBalanceResponse> {
        return safeApiCall<CustomerBalanceResponse>(
            json = json,
            request = {
                httpClient.post("$prefix/api/v1/accounts/balance"){
                    setBody(payload)
                }
            }
        )
    }

    override suspend fun sendMoney(payload: SendMoneyPayload): NetworkResult<SendMoneyResponse> {
        return safeApiCall<SendMoneyResponse>(
            json = json,
            request = {
                httpClient.post("$prefix/api/v1/transactions/send-money"){
                    setBody(payload)
                }
            }
        )
    }

}