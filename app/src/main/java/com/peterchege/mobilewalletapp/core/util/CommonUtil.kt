package com.peterchege.mobilewalletapp.core.util

import android.content.Context
import android.widget.Toast
import androidx.work.WorkInfo

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }