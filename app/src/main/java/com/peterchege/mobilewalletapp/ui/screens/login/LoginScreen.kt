package com.peterchege.mobilewalletapp.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.ui.components.AppButton
import com.peterchege.mobilewalletapp.ui.components.AppInputField
import com.peterchege.mobilewalletapp.ui.components.AppLoader
import com.peterchege.mobilewalletapp.ui.components.Toolbar
import com.peterchege.mobilewalletapp.ui.navigation.Screens

// Composable Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var pinVisible by remember { mutableStateOf(false) }

    // Handle snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }


    LoginScreenContent(
        snackbarHostState = snackbarHostState,
        screenState = screenState,
        pinVisible = pinVisible,
        onChangePinVisibility = { pinVisible = !pinVisible },
        onCustomerIdChange = viewModel::onChangeCustomerId,
        onPinChange = viewModel::onPinChange,
        onLoginClick = {
            viewModel.login(
                onLoginSuccess = {
                    navController.navigate(Screens.HOME_SCREEN)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    snackbarHostState: SnackbarHostState,
    screenState: LoginUiState,
    pinVisible: Boolean,
    onChangePinVisibility: () -> Unit,
    onCustomerIdChange: (String) -> Unit,
    onPinChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current


    AppLoader(isLoading = screenState.isLoading)
    Scaffold(
        containerColor = colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Toolbar(text = "Mobile Wallet App")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo or App Name
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Please login to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Customer ID Field
                AppInputField(
                    label = "Customer ID",
                    inputValue = screenState.customerId,
                    onChangeInputValue = onCustomerIdChange,
                    keyboardType = KeyboardType.Text,
                    showVisibilityOption = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppInputField(
                    label = "PIN",
                    inputValue = screenState.pin,
                    onChangeInputValue = onPinChange,
                    keyboardType = KeyboardType.Number,
                    showVisibilityOption = true,
                    contentDes = "PIN"
                )
                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = "Login",
                    enabled = screenState.isLoading.not() &&
                            screenState.customerId.isNotBlank() &&
                            screenState.pin.isNotBlank(),
                    onClick = onLoginClick
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreenContent(
        snackbarHostState = remember { SnackbarHostState() },
        screenState = LoginUiState(),
        pinVisible = false,
        onChangePinVisibility = {},
        onCustomerIdChange = {},
        onPinChange = {},
        onLoginClick = {}
    )
}