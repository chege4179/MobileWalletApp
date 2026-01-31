package com.peterchege.mobilewalletapp.ui.screens.transactions.local

import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.core.database.entities.OfflineTransaction
import com.peterchege.mobilewalletapp.ui.components.OfflineTransactionCard
import com.peterchege.mobilewalletapp.ui.components.Toolbar
import com.peterchege.mobilewalletapp.ui.components.offlineTransactions
import com.peterchege.mobilewalletapp.ui.theme.MobileWalletAppTheme

@Composable
fun LocalTransactionScreen(
    navController: NavController,
    viewModel: LocalTransactionScreenViewModel = hiltViewModel()
) {
    val offlineTransactions by viewModel.offlineTransactions.collectAsStateWithLifecycle()
    val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()

    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    // Handle snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
        viewModel.clearSnackbar()
    }

    LocalTransactionScreenContent(
        snackbarHostState = snackbarHostState,
        offlineTransactions = offlineTransactions,
        onSyncClick = {
            viewModel.syncTransaction(
                offlineTransaction = it,
                customerLoginResponse = userDetails
            )
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransactionScreenContent(
    snackbarHostState: SnackbarHostState,
    offlineTransactions: List<OfflineTransaction>,
    onSyncClick: (OfflineTransaction) -> Unit,
) {
    Scaffold(
        containerColor = colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Toolbar(text = "Local Transactions")

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
            if (offlineTransactions.isEmpty()){
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(top = 20.dp)
                    ) {
                        Text(
                            text = "No transaction found",
                            style = TextStyle(
                                color = colorScheme.secondary,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = typography.bodyLarge.fontFamily
                            )
                        )
                    }
                }
            }else{
                items(items = offlineTransactions) {
                    OfflineTransactionCard(
                        transaction = it,
                        onSyncClick = onSyncClick
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun LocalTransactionPreview1(){
    MobileWalletAppTheme(
        darkTheme = true,
    ) {
        LocalTransactionScreenContent(
            snackbarHostState = remember { SnackbarHostState() },
            offlineTransactions = offlineTransactions,
            onSyncClick = {},
        )
    }
}

@Preview
@Composable
fun LocalTransactionPreview2(){
    MobileWalletAppTheme(
        darkTheme = false,
    ) {
        LocalTransactionScreenContent(
            snackbarHostState = remember { SnackbarHostState() },
            offlineTransactions = offlineTransactions,
            onSyncClick = {},
        )
    }
}