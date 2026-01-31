package com.peterchege.mobilewalletapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
    text: String,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(10),
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            disabledContainerColor = Color(0xFF919191)
        ),
        shape = shape,
        contentPadding = ButtonDefaults.ContentPadding,
        enabled = enabled
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
            ),
            text = text,
            textAlign = TextAlign.Center
        )
    }
}
