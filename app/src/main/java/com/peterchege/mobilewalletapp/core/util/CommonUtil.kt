package com.peterchege.mobilewalletapp.core.util

import android.content.Context
import android.widget.Toast
import androidx.work.WorkInfo

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }