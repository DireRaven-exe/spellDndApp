package com.example.spellsdnd.navigation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.ui.theme.SpellDndSize
import com.example.spellsdnd.ui.theme.SpellDndStyle
import com.example.spellsdnd.ui.theme.SpellDndTheme

/**
 * Data class, хранящий параметры из пукнта настроек,
 * выбранных пользователем
 * @param selectedLanguage - выбранный язык
 * @param selectedStyle - выбранная тема/стиль
 * @param selectedFontSize - выбранный размер шрифта
 */
data class Settings(
    val selectedLanguage: MutableState<String>,
    val selectedStyle: MutableState<SpellDndStyle>,
    val selectedFontSize: MutableState<SpellDndSize>
)


/**
 * метод для отрисовки параметров
 */
@Composable
fun CardWithDividers(content: @Composable () -> Unit) {
    Card(
        backgroundColor = SpellDndTheme.colors.secondaryBackground,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            content()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

/**
 * метод для отрисовки разделителя между параметрами
 */
@Composable
fun CreateSpacerWithDivider() {
    Spacer(modifier = Modifier.height(10.dp))
    Divider(
        modifier = Modifier
            .height(1.dp)
            .padding(horizontal = 10.dp),
        color = SpellDndTheme.colors.primaryBackground
    )
}