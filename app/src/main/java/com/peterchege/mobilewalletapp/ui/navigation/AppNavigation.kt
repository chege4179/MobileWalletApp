package com.peterchege.mobilewalletapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterchege.mobilewalletapp.ui.screens.home.HomeScreen
import com.peterchege.mobilewalletapp.ui.screens.login.LoginScreen
import com.peterchege.mobilewalletapp.ui.screens.profile.ProfileScreen
import com.peterchege.mobilewalletapp.ui.screens.sendMoney.SendMoneyScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN_SCREEN,
        route = null,
        typeMap = emptyMap(),
    ) {
        composable<Screens.LOGIN_SCREEN> {
            LoginScreen(navController = navController)
        }

        composable<Screens.HOME_SCREEN> {
            HomeScreen(navController = navController)
        }

        composable<Screens.PROFILE_SCREEN> {
            ProfileScreen(navController = navController)
        }
        composable<Screens.SEND_MONEY_SCREEN> {
            SendMoneyScreen(navController = navController)
        }
    }

}