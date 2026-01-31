package com.peterchege.mobilewalletapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun PasswordVisibilityIcon(
    modifier: Modifier = Modifier,
    passwordVisible:Boolean,
    toggleVisibility: () -> Unit,
    tint: Color = colorScheme.primary,
    contentDescription:String = "Balance Visibility"
) {
    val eyeopen = Icons.Default.Visibility
    val eyeclosed = Icons.Default.VisibilityOff
    val icon = if (passwordVisible) {
        eyeopen
    } else {
        eyeclosed
    }
    IconButton(
        modifier = modifier,
        onClick = toggleVisibility
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint =tint
        )
    }
}