package com.example.spellsdnd.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import java.util.Locale


/**
 * Объект, который хранит переменную со списком заклинаний и кастомные шрифты
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
        "necromancy" -> {
            drawableId = R.drawable.necromancy
            backgroundColor = Color(0xFF3B3636)
        }
        "transmutation" -> {
            drawableId = R.drawable.transmutation
            backgroundColor = Color(0xFF933705)
        }
        "evocation" -> {
            drawableId = R.drawable.evocation
            backgroundColor = Color(0xFF355F50)
        }
        "illusion" -> {
            drawableId = R.drawable.illusion
            backgroundColor = Color(0xFF8F2E27)
        }
        "divination" -> {
            drawableId = R.drawable.divination
            backgroundColor = Color(0xFF65395C)
        }
        "abjuration" -> {
            drawableId = R.drawable.abjuration
            backgroundColor = Color(0xFF618022)
        }
        "conjuration" -> {
            drawableId = R.drawable.conjuration
            backgroundColor = Color(0xFF2C3155)
        }
        "enchantment" -> {
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
