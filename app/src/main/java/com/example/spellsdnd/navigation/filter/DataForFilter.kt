package com.example.spellsdnd.navigation.filter

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf


object DataForFilter {
    val spellLevels = (0..9).map { it.toString() }

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

    private val listAllLevels = mutableStateListOf(
        mutableListOf( // Индекс 0 для русского языка
            "Заговор",
            "1й уровень",
            "2й уровень",
            "3й уровень",
            "4й уровень",
            "5й уровень",
            "6й уровень",
            "7й уровень",
            "8й уровень",
            "9й уровень"
        ),
        mutableListOf( // Индекс 1 для английского языка
            "Cantrip",
            "1st-level",
            "2nd-level",
            "3rd-level",
            "4th-level",
            "5th-level",
            "6th-level",
            "7th-level",
            "8th-level",
            "9th-level"
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

    fun getListLevelBySelectedLanguage(selectedLanguage: MutableState<String>) : MutableList<String> {
        return when (selectedLanguage.value) {
            "ru" -> listAllLevels[0]
            else -> listAllLevels[1]
        }
    }
}

data class FilterData(
    val selectedSchools: MutableState<String>,
    val selectedClasses: MutableState<String>,
    val selectedLevels: MutableState<String>
)






