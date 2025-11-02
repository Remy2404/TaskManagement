package com.myphka.taskmanagement.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrand,
    secondary = ToDoBlue,
    tertiary = InProgressOrange,
    background = BackgroundLavender,
    surface = BackgroundWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White
)

@Composable
fun TaskManagementTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
