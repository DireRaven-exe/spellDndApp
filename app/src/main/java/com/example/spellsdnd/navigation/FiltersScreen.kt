package com.example.spellsdnd.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.utils.DarkBlueColorTheme
import com.example.spellsdnd.utils.DndClass
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList
import com.example.spellsdnd.utils.Utils.dndClassMap
import java.util.*


@Composable
fun FiltersScreen(onApplyFilter: (List<SpellDetail>) -> Unit ) {
    // MutableState для хранения выбранных пользователем фильтров
    val selectedSchools = remember { mutableSetOf<String>() }
    val selectedClasses = remember { mutableSetOf<String>() }
    val selectedLevels = remember { mutableSetOf<Int>() }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
    ) {
        item {
            Column {
                SpellLevelsFilter(selectedLevels)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Column {// Панель фильтрации по школам заклинаний
                SpellSchoolFilter(selectedSchools = selectedSchools)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Column {// Панель фильтрации по классам ДнД
                ClassFilter(selectedClasses = selectedClasses)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                FilterListButton(selectedSchools, selectedClasses, selectedLevels, onApplyFilter)
                ClearChangesListButton()
            }
        }
    }
}

@Composable
private fun ClearChangesListButton() {
    Button(
        // Кнопка отмены фильтров
        onClick = {
            spellsList.clear()
            spellsList.addAll(originalSpellsList)
        },
        modifier = Modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DarkBlueColorTheme.bottomBarBackgroundColor,
            contentColor = DarkBlueColorTheme.textColor
        )
    ) {
        Text(stringResource(id = R.string.cancel))
    }
}

/**
 * Метод, реализующий кнопку принятия фильтров
 * @param selectedClasses - выбранные классы
 * @param selectedLevels - выбранные уровни
 * @param selectedSchools - выбранные школы
 * @param onApplyFilter - фильтрация
 */
@Composable
private fun FilterListButton(
    selectedSchools: MutableSet<String>,
    selectedClasses: MutableSet<String>,
    selectedLevels: MutableSet<Int>,
    onApplyFilter: (List<SpellDetail>) -> Unit
) {
    Button(
        // Кнопка применения фильтров
        onClick = {
            // Фильтрация списка заклинаний по выбранным параметрам
            spellsList.removeAll { spell ->
                !spellDetailSet(selectedSchools, selectedClasses, selectedLevels).contains(spell)
            }
            onApplyFilter(spellsList) // Вызов обработчика применения фильтров
        },
        modifier = Modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DarkBlueColorTheme.bottomBarBackgroundColor,
            contentColor = DarkBlueColorTheme.textColor
        )
    ) {
        Text(stringResource(id = R.string.use_filters))
    }
}

/**
 * Метод, который фильтрует список по выбранным параметрам
 * @param selectedClasses - выбранные классы
 * @param selectedLevels - выбранные уровни
 * @param selectedSchools - выбранные школы
 */
private fun spellDetailSet(
    selectedSchools: MutableSet<String>,
    selectedClasses: MutableSet<String>,
    selectedLevels: MutableSet<Int>
): List<SpellDetail> {
    val filteredList = spellsList.filter { spell ->
        val schoolFilter = selectedSchools.isEmpty() ||
                selectedSchools.contains(spell.school.lowercase())
        val classFilter = spell.dnd_class.split(", ").map { className ->
            val dndClass = dndClassMap[className.trim()]
            dndClass
        }.any { dndClass ->
            dndClass.toString().lowercase() in selectedClasses || selectedClasses.isEmpty()
        }
        val levelFilter = selectedLevels.isEmpty() ||
                selectedLevels.contains(spell.level_int)
        schoolFilter && classFilter && levelFilter
    }
    return filteredList
}

/**
 * Панель фильтрации по уровню заклинаний
 * @param selectedLevels - множество выбранных элементов.
 */
@Composable
fun SpellLevelsFilter(selectedLevels: MutableSet<Int>) {
    val spellLevels = (0..9).map { it.toString() }
    Filter(
        label = stringResource(id = R.string.level),
        icon = R.drawable.icon_filter_level,
        items = spellLevels,
        selectedItems = selectedLevels.map { it.toString() }.toMutableSet(),
        onFilterChange = { selectedItems ->
            selectedLevels.clear()
            selectedLevels.addAll(selectedItems.mapNotNull { it.toIntOrNull() })
        }
    )
}

/**
 * Панель фильтрации по школе заклинаний
 * @param selectedSchools - множество выбранных элементов.
 */
@Composable
fun SpellSchoolFilter(selectedSchools: MutableSet<String>) {
    val spellSchoolsRuEn = listOf(
        Pair("evocation", "воплощение"),
        Pair("conjuration", "вызов"),
        Pair("abjuration","ограждение"),
        Pair("transmutation", "преобразование"),
        Pair("enchantment", "очарование"),
        Pair("illusion", "иллюзия"),
        Pair("necromancy","некромантия"),
        Pair("divination", "прорицание")
    )
    val currentLocale: Locale = Locale.getDefault()
    val currentLanguage: String = currentLocale.language

    val schoolsList: List<String> = if (currentLanguage == "ru") {
        spellSchoolsRuEn.map { it.second }
    } else {
        spellSchoolsRuEn.map { it.first }
    }


    Filter(
        label = stringResource(id = R.string.school),
        icon = R.drawable.icon_filter_school,
        items = schoolsList,
        selectedItems = selectedSchools,
        onFilterChange = { schools ->
            selectedSchools.clear()
            selectedSchools.addAll(schools)
        }
    )
}

/**
 * Панель фильтрации по классу персонажа
 * @param selectedClasses - множество выбранных элементов.
 */
@Composable
fun ClassFilter(selectedClasses: MutableSet<String>) {
    val dndClasses = enumValues<DndClass>().map { it.name.lowercase() }.toSet()
    Filter(
        label = stringResource(id = R.string.dnd_class),
        icon = R.drawable.icon_filter_class,
        items = dndClasses.toList(),
        selectedItems = selectedClasses,
        onFilterChange = { classes ->
            selectedClasses.clear()
            selectedClasses.addAll(classes)
        }
    )
}
