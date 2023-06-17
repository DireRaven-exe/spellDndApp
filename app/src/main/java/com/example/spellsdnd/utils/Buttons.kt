package com.example.spellsdnd.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.ui.theme.SpellDndTheme

@Composable
fun ButtonNotSelected(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonText: String
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 3.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.buttonBackgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = buttonText, color = SpellDndTheme.colors.buttonContentColor, modifier = textModifier)
    }
}

@Composable
fun ButtonSelected(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonText: String
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 3.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.primaryBackground,
            contentColor = SpellDndTheme.colors.primaryText
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = buttonText, modifier = textModifier)
    }
}