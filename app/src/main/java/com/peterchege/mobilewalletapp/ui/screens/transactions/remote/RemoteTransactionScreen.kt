package com.peterchege.mobilewalletapp.ui.screens.transactions.remote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.ui.components.AppLoader
import com.peterchege.mobilewalletapp.ui.components.Toolbar
import com.peterchege.mobilewalletapp.ui.components.TransactionCard
import com.peterchege.mobilewalletapp.ui.components.remoteTransactions
import com.peterchege.mobilewalletapp.ui.theme.MobileWalletAppTheme

@Composable
fun RemoteTransactionScreen(
    navController: NavController,
    viewModel: RemoteTransactionScreenViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()
    LaunchedEffect(userDetails) {
        if (userDetails.customerId?.isNotBlank() == true) {
            viewModel.fetchLast100Transactions(customerId = userDetails.customerId ?: "")
        }
    }

    RemoteTransactionScreenContent(screenState = screenState)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteTransactionScreenContent(
    screenState: RemoteTransactionScreenState,
) {
    AppLoader(isLoading = screenState.isLoading)
    Scaffold(
        containerColor = colorScheme.surface,
        topBar = {
            Toolbar(text = "Remote Transactions")
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (screenState.transactions.isEmpty()) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(top = 20.dp)
                    ) {
                        Text(
                            text = "No transactions found",
                            style = TextStyle(
                                color = colorScheme.secondary,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = typography.bodyLarge.fontFamily
                            )
                        )
                    }
                }
            } else {
                items(items = screenState.transactions) {
                    TransactionCard(transaction = it)
                }
            }
        }
    }
}


@Preview
@Composable
fun RemoteTransactionPreview1() {
    MobileWalletAppTheme(
        darkTheme = true,
    ) {
        RemoteTransactionScreenContent(
            screenState = RemoteTransactionScreenState(
                transactions = remoteTransactions
            )
        )
    }
}


@Preview
@Composable
fun RemoteTransactionPreview2() {
    MobileWalletAppTheme(
        darkTheme = false,
    ) {
        RemoteTransactionScreenContent(
            screenState = RemoteTransactionScreenState(
                transactions = remoteTransactions
            )
        )
    }
}