package com.example.spellsdnd.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import org.intellij.lang.annotations.Language
import java.util.Locale


/**
 * Объект, который хранит полезные параметры (список заклинаний, классов, школ и т.д.)
 */
object Utils {
    val customFont_im_fell_english_regular = FontFamily(
        Font(R.font.imfellenglish_regular, FontWeight.Normal, FontStyle.Italic)
    )
    val customFont_im_fell_english_italic = FontFamily(
        Font(R.font.imfellenglish_italic, FontWeight.Normal, FontStyle.Italic)
    )

    var originalSpellsList = mutableStateListOf<SpellDetail>() // Создаем MutableStateList для заклинаний
    var spellsList = mutableStateListOf<SpellDetail>() // Создаем MutableStateList для заклинаний
    val spellsFavoritesList = mutableStateListOf<SpellDetail>()// Создаем MutableStateList для заклинаний

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
 * Метод, который проверяет, к какой школе заклинаний относится переданное ей заклинание
 * Возвращает цвет и картинку с этой школой
 */
@Composable
fun schoolCheck(spellDetail: SpellDetail): Pair<Int, Color> {
    val drawableId: Int
    val backgroundColor: Color
    when (spellDetail.school.lowercase(Locale.getDefault())) {
        stringResource(R.string.necromancy) -> {
            drawableId = R.drawable.necromancy
            backgroundColor = Color(0xFF3B3636)
        }
        stringResource(R.string.transmutation) -> {
            drawableId = R.drawable.transmutation
            backgroundColor = Color(0xFF933705)
        }
        stringResource(R.string.evocation) -> {
            drawableId = R.drawable.evocation
            backgroundColor = Color(0xFF355F50)
        }
        stringResource(R.string.illusion) -> {
            drawableId = R.drawable.illusion
            backgroundColor = Color(0xFF8F2E27)
        }
        stringResource(R.string.divination) -> {
            drawableId = R.drawable.divination
            backgroundColor = Color(0xFF65395C)
        }
        stringResource(R.string.abjuration) -> {
            drawableId = R.drawable.abjuration
            backgroundColor = Color(0xFF618022)
        }
        stringResource(R.string.conjuration) -> {
            drawableId = R.drawable.conjuration
            backgroundColor = Color(0xFF2C3155)
        }
        stringResource(R.string.enchantment) -> {
            drawableId = R.drawable.conjuration
            backgroundColor = Color(0xFF783674)
        }
        else -> {
            drawableId = R.drawable.neutural_magic_spell
            backgroundColor = Color(0xFF5B3EC0)
            Log.e("not ok", "path don't find")
        }
    }
    return Pair(drawableId, backgroundColor)
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
fun checkDuration(currentString: String): String {
    return if (currentString == "INSTANTANEOUS") "INSTANT"
    else currentString
}
