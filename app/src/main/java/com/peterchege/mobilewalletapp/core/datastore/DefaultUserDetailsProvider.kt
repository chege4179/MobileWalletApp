package com.peterchege.mobilewalletapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import kotlin.jvm.java

interface DefaultUserDetailsProvider {

    val userDetails: Flow<CustomerLoginResponse>
    suspend fun setUserDetails(details: CustomerLoginResponse)

}


class DefaultUserDetailsProviderImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DefaultUserDetailsProvider {

    val TAG = DefaultUserDetailsProviderImpl::class.java.simpleName

    val KEY = stringPreferencesKey("userDetails")
    override val userDetails: Flow<CustomerLoginResponse> = dataStore.data.map { preferences ->
        try {
            val data = preferences[KEY] ?: "{}"
            json.decodeFromString<CustomerLoginResponse>(data)
        } catch (error: Exception) {
            Timber.tag(TAG).e(error)
            CustomerLoginResponse()
        }
    }

    override suspend fun setUserDetails(details: CustomerLoginResponse) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it[KEY] = json.encodeToString(details)
            }
        }
    }
}