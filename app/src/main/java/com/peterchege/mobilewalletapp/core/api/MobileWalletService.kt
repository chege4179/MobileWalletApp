package com.peterchege.mobilewalletapp.core.api

import com.peterchege.mobilewalletapp.core.models.payloads.CustomerBalancePayload
import com.peterchege.mobilewalletapp.core.models.payloads.CustomerLoginPayload
import com.peterchege.mobilewalletapp.core.models.payloads.SendMoneyPayload
import com.peterchege.mobilewalletapp.core.models.responses.CustomerBalanceResponse
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.models.responses.SendMoneyResponse
import com.peterchege.mobilewalletapp.core.util.NetworkResult

interface MobileWalletService {

    suspend fun customerLogin(payload: CustomerLoginPayload): NetworkResult<CustomerLoginResponse>
    suspend fun fetchAccountBalanceByCustomerId(payload: CustomerBalancePayload): NetworkResult<CustomerBalanceResponse>

    suspend fun sendMoney(payload: SendMoneyPayload): NetworkResult<SendMoneyResponse>
}