package com.example.spellsdnd.ui.theme

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp


data class SpellDndColors(
    val primaryText: Color,
    val primaryBackground: Color,

    val secondaryText: Color,
    val secondaryBackground: Color,

    val tintColor: Color,

    val primaryIcon: Color,
    val secondaryIcon: Color,

    val primaryButtonTextColor: Color,
    val secondaryButtonTextColor: Color,

    val buttonBackgroundColor: Color,
    val buttonColor: Color
)
data class SpellDndTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle
)

data class SpellDndShape(
    val padding: Dp,
    val cornersStyle: Shape
)

object SpellDndTheme {
    val colors: SpellDndColors
    @Composable
    get() = LocalSpellDndColors.current
    val typography: SpellDndTypography
    @Composable
    get() = LocalSpellDndTypography.current
}

enum class SpellDndStyle {
    DarkBlue, Dark, Light
}

enum class SpellDndSize {
    Small, Medium, Big
}

val LocalSpellDndColors = staticCompositionLocalOf<SpellDndColors> {
    error("No colors provided")
}
val LocalSpellDndTypography = staticCompositionLocalOf<SpellDndTypography> {
    error("No font provided")
}

@SuppressLint("NewApi")
@Composable
fun SpellDndTheme(
    style: SpellDndStyle = SpellDndStyle.DarkBlue,
    textSize: SpellDndSize = SpellDndSize.Medium,
    content: @Composable () -> Unit
) {
    val colors = when(style) {
        SpellDndStyle.DarkBlue -> SpellDndColors(
            primaryText = DarkBluePalette.primaryText,
            primaryBackground = DarkBluePalette.primaryBackground,

            secondaryText = DarkBluePalette.secondaryText,
            secondaryBackground = DarkBluePalette.secondaryBackground,

            tintColor = DarkBluePalette.tintColor,

            primaryIcon = DarkBluePalette.primaryIcon,
            secondaryIcon = DarkBluePalette.secondaryIcon,

            primaryButtonTextColor = DarkBluePalette.primaryButtonTextColor,
            secondaryButtonTextColor = DarkBluePalette.secondaryButtonTextColor,

            buttonBackgroundColor = DarkBluePalette.buttonBackgroundColor,
            buttonColor = DarkBluePalette.buttonColor
        )
        SpellDndStyle.Light -> SpellDndColors(
            primaryText = baseLightPalette.primaryText,
            primaryBackground = baseLightPalette.primaryBackground,

            secondaryText = baseLightPalette.secondaryText,
            secondaryBackground = baseLightPalette.secondaryBackground,

            tintColor = baseLightPalette.tintColor,

            primaryIcon = baseLightPalette.primaryIcon,
            secondaryIcon = baseLightPalette.secondaryIcon,

            primaryButtonTextColor = baseLightPalette.primaryButtonTextColor,
            secondaryButtonTextColor = baseLightPalette.secondaryButtonTextColor,

            buttonBackgroundColor = baseLightPalette.buttonBackgroundColor,
            buttonColor = baseLightPalette.buttonColor
        )
        else -> SpellDndColors(
            primaryText = baseDarkPalette.primaryText,
            primaryBackground = baseDarkPalette.primaryBackground,

            secondaryText = baseDarkPalette.secondaryText,
            secondaryBackground = baseDarkPalette.secondaryBackground,

            tintColor = baseDarkPalette.tintColor,

            primaryIcon = baseDarkPalette.primaryIcon,
            secondaryIcon = baseDarkPalette.secondaryIcon,

            primaryButtonTextColor = baseDarkPalette.primaryButtonTextColor,
            secondaryButtonTextColor = baseDarkPalette.secondaryButtonTextColor,

            buttonBackgroundColor = baseDarkPalette.buttonBackgroundColor,
            buttonColor = baseDarkPalette.buttonColor
        )
    }
    val typography = SpellDndTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                SpellDndSize.Small -> 24.sp
                SpellDndSize.Medium -> 28.sp
                SpellDndSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                SpellDndSize.Small -> 14.sp
                SpellDndSize.Medium -> 16.sp
                SpellDndSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                SpellDndSize.Small -> 14.sp
                SpellDndSize.Medium -> 16.sp
                SpellDndSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        )
    )

    CompositionLocalProvider(
        LocalSpellDndColors provides colors,
        LocalSpellDndTypography provides typography,
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
    val lineColor = Color(0xFF485866) // Цвет иконки для остальных экранов

}