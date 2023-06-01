package com.example.spellsdnd.utils

import androidx.compose.runtime.mutableStateListOf
import com.example.spellsdnd.data.SpellDetail

object MutableListManager {
    var originalSpellsList = mutableStateListOf<SpellDetail>() // Создаем MutableStateList для заклинаний
    var spellsList = mutableStateListOf<SpellDetail>() // Создаем MutableStateList для заклинаний

}