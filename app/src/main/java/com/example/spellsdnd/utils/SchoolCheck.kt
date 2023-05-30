package com.example.spellsdnd.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail

/**
 * Метод, который проверяет, к какой школе заклинаний относится переданное ей заклинание
 * Возвращает цвет и картинку с этой школой
 */
@Composable
fun schoolCheck(spellDetail: SpellDetail): Pair<Int, Color> {
    val drawableId: Int
    val backgroundColor: Color
    when (spellDetail.school.lowercase()) {
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