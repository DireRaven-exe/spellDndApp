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

class FilterData(
    val selectedSchools: MutableSet<String>,
    val selectedClasses: MutableSet<String>,
    val selectedLevels: MutableSet<Int>
) {
    fun saveToSharedPreferences(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedSchools", selectedSchools)
        editor.putStringSet("selectedClasses", selectedClasses)
        editor.putStringSet("selectedLevels", selectedLevels.map { it.toString() }.toSet())
        editor.apply()
    }

    fun loadFromSharedPreferences(sharedPreferences: SharedPreferences) {
        selectedSchools.clear()
        selectedSchools.addAll(sharedPreferences.getStringSet("selectedSchools", emptySet()) ?: emptySet())

        selectedClasses.clear()
        selectedClasses.addAll(sharedPreferences.getStringSet("selectedClasses", emptySet()) ?: emptySet())

        selectedLevels.clear()
        val selectedLevelsStrSet = sharedPreferences.getStringSet("selectedLevels", emptySet()) ?: emptySet()
        selectedLevels.addAll(selectedLevelsStrSet.mapNotNull { it.toIntOrNull() })
    }
}






