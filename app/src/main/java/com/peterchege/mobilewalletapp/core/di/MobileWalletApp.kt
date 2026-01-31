package com.peterchege.mobilewalletapp.core.di

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MobileWalletApp :Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

    @OptIn(ExperimentalComposeRuntimeApi::class)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Composer.setDiagnosticStackTraceMode(mode = ComposeStackTraceMode.Auto)

    }
}