package com.peterchege.mobilewalletapp.core.di

import android.app.Application
import com.peterchege.mobilewalletapp.BuildConfig
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.peterchege.mobilewalletapp.core.api.MobileWalletService
import com.peterchege.mobilewalletapp.core.api.MobileWalletServiceImpl
import com.peterchege.mobilewalletapp.core.database.dao.OfflineTransactionDao
import com.peterchege.mobilewalletapp.core.database.database.MobileWalletAppDatabase
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProvider
import com.peterchege.mobilewalletapp.core.datastore.DefaultUserDetailsProviderImpl
import com.peterchege.mobilewalletapp.core.util.Constants
import com.peterchege.mobilewalletapp.core.util.IoDispatcher
import com.peterchege.mobilewalletapp.core.work.SyncTransactionsWorkManager
import com.peterchege.mobilewalletapp.core.work.SyncTransactionsWorkManagerImpl
import com.peterchege.mobilewalletapp.core.work.SyncTransactionsWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences",
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() }
    ),
)


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @IoDispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideUserDetailsProvider(
        json: Json,
        dataStore: DataStore<Preferences>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DefaultUserDetailsProvider {
        return DefaultUserDetailsProviderImpl(
            json = json,
            dataStore = dataStore,
            ioDispatcher = ioDispatcher
        )

    }

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context = context)
            .collector(ChuckerCollector(context = context))
            .maxContentLength(length = 250000L)
            .redactHeaders(headerNames = emptySet())
            .alwaysReadResponseBody(enable = false)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        return HttpLoggingInterceptor().also {
            it.level = level
        }
    }


    @Provides
    @Singleton
    fun provideHttpClientEngine(
        chuckerInterceptor: ChuckerInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ): HttpClientEngine = OkHttp.create {
        addInterceptor(chuckerInterceptor)
        addInterceptor(loggingInterceptor)
    }


    @Singleton
    @Provides
    fun provideHttpClient(
        httpClientEngine: HttpClientEngine,
        json: Json,
    ): HttpClient {
        return HttpClient(httpClientEngine) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url(Constants.BASE_URL)
                header(key = HttpHeaders.ContentType, value = ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(WebSockets)
        }
    }


    @Singleton
    @Provides
    fun provideApiService(
        json: Json,
        httpClient: HttpClient,
    ): MobileWalletService {

        return MobileWalletServiceImpl(
            json = json,
            httpClient = httpClient,
        )
    }

    @Provides
    @Singleton
    fun provideMobileWalletAppDatabase(app: Application): MobileWalletAppDatabase {
        return Room.databaseBuilder(
            app,
            MobileWalletAppDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideOfflineTransactionDao(database: MobileWalletAppDatabase): OfflineTransactionDao {
        return database.offlineTransactionDao
    }

    @Provides
    @Singleton
    fun provideSyncTransactionsWorkManager(
        @ApplicationContext context: Context,
    ): SyncTransactionsWorkManager {
        return SyncTransactionsWorkManagerImpl(context = context)

    }




}