package com.peterchege.mobilewalletapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF80BA27),
    onPrimary = Color(0xFFF0EAEA),
    primaryContainer = Color(0xFF0D3274),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF542100),
    secondaryContainer = Color(0xFFF8955B),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFF0000005e),
    onTertiary = Color(0xFF542100),
    tertiaryContainer = Color(0xFFEC6A00),
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFFF0000),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1B110C),
    onBackground = Color(0xFFF4DED4),
    surface = Color(0xFF000000),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFF171717),
    onSurfaceVariant = Color(0xFFFFFFFF),
    outline = Color(0xFF262626),
    outlineVariant = Color(0xFF171717),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFF4DED4),
    inverseOnSurface = Color(0xFF3A2E27),
    inversePrimary =  Color(0XFF00513A),
    surfaceDim = Color(0xFF242424),
    surfaceBright = Color(0xFF443630),
    surfaceContainerLowest = Color(0xFF160C07),
    surfaceContainerLow = Color(0xFF241913),
    surfaceContainer = Color(0xFF0A0A0A),
    surfaceContainerHigh = Color(0xFF342721),
    surfaceContainerHighest = Color(0xFF3F322B),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF80BA27),
    onPrimary = Color(0xFFF0EAEA),
    primaryContainer = Color(0xFF0D3274),
    onPrimaryContainer = Color(0xFF2C0E00),
    secondary = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFA878),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFF0000005e),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFF7E29),
    onTertiaryContainer = Color(0xFF2C0E00),
    error = Color(0xFFFF0000),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFF8F6),
    onBackground = Color(0xFF241913),
    surface = Color(0XFFFFFFFF),
    //onSurface =  Color(0xFFF2F4F7),
    onSurface =  Color(0xFFF8F8F8),
    surfaceVariant = Color(0xFFFFF8F6),
    onSurfaceVariant = Color(0xFF241913),
    outline = Color(0xFF262626),
    outlineVariant = Color(0xFFECECEC),
    scrim = Color(0xFF050505),
    inverseSurface =  Color(0xFF3A2E27),
    inverseOnSurface = Color(0xFFFFEDE5),
    inversePrimary = Color(0XFF00513A),
    surfaceDim =  Color(0xFFECECEC),
    surfaceBright = Color(0xFFFFF8F6),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF1EB),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFFAE4DA),
    surfaceContainerHighest =  Color(0xFFF4DED4),
)

@Composable
fun MobileWalletAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}