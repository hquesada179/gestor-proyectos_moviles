package com.helbertquesada.gestor_proyectos_moviles.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = TextPrimary,
    secondary = AccentViolet,
    onSecondary = TextPrimary,
    tertiary = AccentVioletLight,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    error = ErrorColor,
    onError = TextPrimary,
)

@Composable
fun Gestorproyectos_movilesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun appTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = DarkCardElevated,
    unfocusedContainerColor = DarkCardElevated,
    errorContainerColor = DarkCardElevated,
    cursorColor = AccentBlue,
    focusedBorderColor = AccentBlue,
    unfocusedBorderColor = BorderDefault,
    errorBorderColor = ErrorColor,
    focusedLabelColor = AccentBlueLight,
    unfocusedLabelColor = TextSecondary,
    errorLabelColor = ErrorColor,
    errorSupportingTextColor = ErrorColor,
    focusedTrailingIconColor = TextSecondary,
    unfocusedTrailingIconColor = TextSecondary,
    errorTrailingIconColor = ErrorColor,
    focusedPlaceholderColor = TextHint,
    unfocusedPlaceholderColor = TextHint,
    errorPlaceholderColor = TextHint,
)
