package com.peterchege.mobilewalletapp.ui.screens.sendMoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.core.util.toast
import com.peterchege.mobilewalletapp.ui.components.AppLoader


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
        if (userDetails.customerId?.isNotBlank() == true){
            viewModel.fetchAccountBalance(
                customerId = userDetails.customerId ?:"",
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
            if (screenState.customerBalance != null){
                viewModel.onSubmit(
                    customerBalanceResponse = screenState.customerBalance!!,
                )
            }else{
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Send Money") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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

            OutlinedTextField(
                value = screenState.creditAccountNumber,
                onValueChange = onChangeCreditAccountNumber,
                label = { Text("Account To ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !screenState.isLoading,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = screenState.amount,
                onValueChange = onChangeAmount,
                label = { Text("PIN") },
                modifier = Modifier.fillMaxWidth(),
                enabled = screenState.isLoading.not(),
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSubmit()
                    }
                ),

                )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = screenState.isLoading.not() &&
                        screenState.creditAccountNumber.isNotBlank() &&
                        screenState.amount.isNotBlank()
            ) {
                if (screenState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text("Submit")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
