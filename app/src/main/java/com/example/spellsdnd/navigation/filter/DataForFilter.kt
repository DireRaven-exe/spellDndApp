package com.example.spellsdnd.navigation.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf


object DataForFilter {
    private val listAllSchools = mutableStateListOf(
        mutableListOf( // Индекс 0 для русского языка
            "некромантия",
            "воплощение",
            "иллюзия",
            "преобразование",
            "прорицание",
            "очарование",
            "ограждение",
            "вызов"
        ),
        mutableListOf( // Индекс 1 для английского языка
            "necromancy",
            "evocation",
            "illusion",
            "transmutation",
            "divination",
            "enchantment",
            "abjuration",
            "conjuration"
        )
    )

    private val listAllClasses = mutableStateListOf(
        mutableListOf( // Индекс 0 для русского языка
            "Варвар",
            "Изобретатель",
            "Бард",
            "Жрец",
            "Друид",
            "Воин",
            "Монах",
            "Паладин",
            "Следопыт",
            "Плут",
            "Чародей",
            "Колдун",
            "Волшебник",
            "Отсутствует"
        ),
        mutableListOf( // Индекс 1 для английского языка
            "Barbarian",
            "Arificer",
            "Bard",
            "Cleric",
            "Druid",
            "Fighter",
            "Monk",
            "Paladin",
            "Ranger",
            "Rogue",
            "Sorcerer",
            "Warlock",
            "Wizard",
            "Empty"
        )
    )


    fun getListSchoolsBySelectedLanguage(selectedLanguage: MutableState<String>) : MutableList<String> {
        return when (selectedLanguage.value) {
            "ru" -> listAllSchools[0]
            else -> listAllSchools[1]
        }
    }

    fun getListClassesBySelectedLanguage(selectedLanguage: MutableState<String>) : MutableList<String> {
        return when (selectedLanguage.value) {
            "ru" -> listAllClasses[0]
            else -> listAllClasses[1]
        }
    }
}







