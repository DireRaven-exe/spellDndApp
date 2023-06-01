package com.example.spellsdnd.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SpellsDnDTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

object DarkBlueColorTheme {
    val textColor = Color(0xFFFFFFFF)

    val mainBackgroundColor = Color(0xFF151829)
    val bottomBarBackgroundColor = Color(0xFF222644)


    val textFieldTitleColor = Color(0xFF6D6C61)
    val textFieldFocusedIndicatorColor = Color(0xFF5B649E)
    val textFieldCursorColor = Color(0xFF5B649E)
    val textFieldUnfocusedIndicatorColor = Color(0xFF5B649E)

    val textButtonColor = Color(0xFF8F9EF8)

    //SETTINGS DROP DOWN MENU
    val dropdownMenuBackgroundColor = Color(0xFF222644)

    // NAVIGATION BOTTOM COLORS
    val screenActiveColor = Color(0xFF5EB4F6) // Цвет иконки, когда находимся на соответствующем экране
    val screenInactiveColor = Color(0xFF7993AA) // Цвет иконки для остальных экранов

}


object DarkColorTheme {
    val mainBackgroundColor = Color(0xFF000000)
    val bottomBarBackgroundColor = Color(0xFF1E1E1F)
}