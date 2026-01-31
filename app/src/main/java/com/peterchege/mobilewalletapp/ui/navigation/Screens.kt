package com.peterchege.mobilewalletapp.ui.navigation

import kotlinx.serialization.Serializable


sealed interface Screens {

    @Serializable
    data object LOGIN_SCREEN : Screens

    @Serializable
    data object HOME_SCREEN : Screens

    @Serializable
    data object PROFILE_SCREEN : Screens

    @Serializable
    data object SEND_MONEY_SCREEN : Screens


}