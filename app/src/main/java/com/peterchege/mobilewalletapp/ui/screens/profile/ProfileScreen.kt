package com.peterchege.mobilewalletapp.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.mobilewalletapp.core.models.responses.CustomerLoginResponse
import com.peterchege.mobilewalletapp.ui.components.AppButton
import com.peterchege.mobilewalletapp.ui.components.AppText
import com.peterchege.mobilewalletapp.ui.components.Toolbar

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    val userDetails by viewModel.userDetails.collectAsStateWithLifecycle()

    ProfileScreenContent(
        userDetails = userDetails
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    userDetails: CustomerLoginResponse,
) {

    Scaffold(
        containerColor = colorScheme.surface,
        topBar = {
            Toolbar(text = "Profile")

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
            AppText(text = "Name: ${userDetails.customerName}")
            Spacer(modifier = Modifier.height(10.dp))
            AppText(text = "Customer ID: ${userDetails.customerId}")
            Spacer(modifier = Modifier.height(10.dp))
            AppText(text = "Email: ${userDetails.email}")
            Spacer(modifier = Modifier.height(10.dp))

        }

    }

}