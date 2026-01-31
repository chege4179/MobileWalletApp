package com.peterchege.mobilewalletapp.ui.screens.home

import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.core.models.responses.CustomerBalanceResponse
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.core.util.safeApiCall
import com.peterchege.mobilewalletapp.ui.components.AppButton
import com.peterchege.mobilewalletapp.ui.components.AppLoader
import com.peterchege.mobilewalletapp.ui.navigation.Screens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var isBalanceDialogVisible by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(screenState.snackbarMessage) {
        screenState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    HomeScreenContent(
        screenState = screenState,
        isBalanceDialogVisible = isBalanceDialogVisible,
        onDismissRequest = { isBalanceDialogVisible = false },
        snackbarHostState = snackbarHostState,
        userDetails = userDetails,
        onClickCheckBalance = {
            viewModel.fetchAccountBalance(
                customerId = userDetails.customerId ?: "",
                onSuccess = {
                    isBalanceDialogVisible = true
                }
            )
        },
        onClickSendMoney = {
            navController.navigate(Screens.SEND_MONEY_SCREEN)
        },
        onClickViewProfile = {
            navController.navigate(Screens.PROFILE_SCREEN)
        },
        onClickViewStatement = {

        },
        onClickViewLocalTransactions = {

        },
        onClickLogout = {
            viewModel.onClickLogout(
                onSuccess = {
                    navController.navigate(Screens.LOGIN_SCREEN)
                }
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    screenState: HomeScreenState,
    isBalanceDialogVisible: Boolean,
    onDismissRequest: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    userDetails: CustomerLoginResponse,
    onClickCheckBalance: () -> Unit,
    onClickSendMoney: () -> Unit,
    onClickViewProfile: () -> Unit,
    onClickViewStatement: () -> Unit,
    onClickViewLocalTransactions: () -> Unit,
    onClickLogout: () -> Unit,
) {
    AppLoader(isLoading = screenState.isLoading)
    if (isBalanceDialogVisible) {
        BasicAlertDialog(
            modifier = Modifier.background(colorScheme.background),
            onDismissRequest = onDismissRequest,
            content = {
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .height(120.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(text = "Account Number:  ${screenState.customerBalance?.accountNo}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Balance :  ${screenState.customerBalance?.balance}")
                    Spacer(modifier = Modifier.height(10.dp))
                    AppButton(
                        text = "Dismiss",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismissRequest
                    )
                }
            },
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Welcome ${userDetails.customerName}") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppButton(
                text = "Check Balance",
                onClick = onClickCheckBalance
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                text = "Send Money",
                onClick = onClickSendMoney
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                text = "View Profile",
                onClick = onClickViewProfile
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                text = "View Statement",
                onClick = onClickViewStatement
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                text = "View Local Transactions",
                onClick = onClickViewLocalTransactions
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                text = "Log out",
                onClick = onClickLogout
            )
        }

    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        userDetails = CustomerLoginResponse(),
        onClickCheckBalance = {},
        onClickSendMoney = {},
        onClickViewProfile = {},
        onClickViewStatement = {},
        onClickViewLocalTransactions = {},
        onClickLogout = {},
        snackbarHostState = SnackbarHostState(),
        screenState = HomeScreenState(),
        isBalanceDialogVisible = true,
        onDismissRequest = {

        }
    )
}