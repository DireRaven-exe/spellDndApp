package com.example.spellsdnd.navigation.settings

import androidx.compose.runtime.MutableState
import com.example.spellsdnd.ui.theme.SpellDndSize
import com.example.spellsdnd.ui.theme.SpellDndStyle

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
