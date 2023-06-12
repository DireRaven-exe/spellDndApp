package com.example.spellsdnd.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail


/**
 * Объект, который хранит полезные параметры (список заклинаний, классов, школ и т.д.)
 */
object Utils {
    val customFont_im_fell_english_regular = FontFamily(
        Font(R.font.imfellenglish_regular, FontWeight.Normal, FontStyle.Italic)
    )

    val dndClassMap = mapOf(
        "Бард" to DndClass.BARD,
        "Bard" to DndClass.BARD,
        "Варвар" to DndClass.BARBARIAN,
        "Barbarian" to DndClass.BARBARIAN,
        "Воин" to DndClass.FIGHTER,
        "Fighter" to DndClass.FIGHTER,
        "Волшебник" to DndClass.WIZARD,
        "Wizard" to DndClass.WIZARD,
        "Друид" to DndClass.DRUID,
        "Druid" to DndClass.DRUID,
        "Жрец" to DndClass.CLERIC,
        "Cleric" to DndClass.CLERIC,
        "Изобретатель" to DndClass.ARTIFICER,
        "Artificer" to DndClass.ARTIFICER,
        "Колдун" to DndClass.WARLOCK,
        "Warlock" to DndClass.WARLOCK,
        "Монах" to DndClass.MONK,
        "Monk" to DndClass.MONK,
        "Паладин" to DndClass.PALADIN,
        "Paladin" to DndClass.PALADIN,
        "Плут" to DndClass.ROGUE,
        "Rogue" to DndClass.ROGUE,
        "Следопыт" to DndClass.RANGER,
        "Ranger" to DndClass.RANGER,
        "Чародей" to DndClass.SORCERER,
        "Sorcerer" to DndClass.SORCERER,
        "" to DndClass.EMPTY,
    )
}


/**
 * Проверяет длину строки
 */
fun isLongText(currentString: String) : String {
    val maxLength = 15
    return if (currentString.length > maxLength) {
        currentString.substring(0, maxLength - 3) + "..."
    } else {
        currentString
    }
}

/**
 * Метод костыль, в будущем его дополню и буду через него обрабатывать текст :)
 */
fun formatParams(currentString: String): String {
    return when (currentString) {
        "INSTANTANEOUS" -> "INSTANT"
        else -> currentString
    }
}
