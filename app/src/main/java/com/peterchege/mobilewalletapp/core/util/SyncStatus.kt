package com.peterchege.mobilewalletapp.core.util

enum class SyncStatus(val value: String) {
    QUEUED("QUEUED"),
    SYNCING("SYNCING"),
    SYNCED("SYNCED"),
    FAILED("FAILED")
}