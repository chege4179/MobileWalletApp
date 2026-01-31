package com.peterchege.mobilewalletapp.ui.screens.sendMoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.core.util.toast
import com.peterchege.mobilewalletapp.ui.components.AppButton
import com.peterchege.mobilewalletapp.ui.components.AppInputField
import com.peterchege.mobilewalletapp.ui.components.AppLoader
import com.peterchege.mobilewalletapp.ui.components.Toolbar


@Composable
fun SendMoneyScreen(
    navController: NavController,
    viewModel: SendMoneyScreenViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(screenState.snackbarMessage) {
        screenState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    LaunchedEffect(userDetails) {
        if (userDetails.customerId?.isNotBlank() == true) {
            viewModel.fetchAccountBalance(
                customerId = userDetails.customerId ?: "",
                onSuccess = {

                }
            )
        }
    }

    AppLoader(isLoading = isSyncing)

    SendMoneyScreenContent(
        snackbarHostState = snackbarHostState,
        screenState = screenState,
        onChangeCreditAccountNumber = viewModel::onChangeCreditAccountNumber,
        onChangeAmount = viewModel::onChangeAmount,
        onSubmit = {
            if (screenState.customerBalance != null) {
                viewModel.onSubmit(
                    customerBalanceResponse = screenState.customerBalance!!,
                )
            } else {
                context.toast(msg = "Failed to retrieve debiting account")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreenContent(
    snackbarHostState: SnackbarHostState,
    screenState: SendMoneyScreenState,
    onChangeCreditAccountNumber: (String) -> Unit,
    onChangeAmount: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current


    Scaffold(
        containerColor = colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Toolbar(text = "Send Money")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            AppInputField(
                label = "Account To",
                inputValue = screenState.creditAccountNumber,
                onChangeInputValue = onChangeCreditAccountNumber,
                keyboardType = KeyboardType.Text,
                showVisibilityOption = false
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppInputField(
                label = "Amount",
                inputValue = screenState.amount,
                onChangeInputValue = onChangeAmount,
                keyboardType = KeyboardType.Number,
                showVisibilityOption = false
            )
            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Submit",
                enabled = screenState.isLoading.not() &&
                        screenState.creditAccountNumber.isNotBlank() &&
                        screenState.amount.isNotBlank(),
                onClick = onSubmit
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
