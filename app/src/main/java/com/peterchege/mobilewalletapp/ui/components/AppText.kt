package com.peterchege.mobilewalletapp.ui.components

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colorScheme.secondary,
    fontSize: Int = 14
) {
    Text(
        text = text,
        textAlign = TextAlign.Left,
        modifier = modifier,
        fontFamily = typography.bodyLarge.fontFamily,
        fontWeight = FontWeight.Bold,
        color = color,
        style = TextStyle(fontSize = fontSize.sp)
    )

}