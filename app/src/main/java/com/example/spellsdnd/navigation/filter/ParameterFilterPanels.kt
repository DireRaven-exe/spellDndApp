package com.example.spellsdnd.navigation.filter

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.example.spellsdnd.R

/**
 * Панель фильтрации по уровню заклинаний
 * @param selectedLevels - множество выбранных элементов.
 */
@Composable
fun SpellLevelsFilter(selectedLevels: MutableSet<Int>) {
    val spellLevels = (0..9).map { it.toString() }
    FilterPanel(
        label = stringResource(id = R.string.level),
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
fun SpellSchoolFilter(selectedLanguage: MutableState<String>, selectedSchools: MutableSet<String>) {
    FilterPanel(
        label = stringResource(id = R.string.school),
        items = DataForFilter.getListSchoolsBySelectedLanguage(selectedLanguage = selectedLanguage),
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
fun ClassFilter(selectedLanguage: MutableState<String>, selectedClasses: MutableSet<String>) {
    FilterPanel(
        label = stringResource(id = R.string.dnd_class),
        items = DataForFilter.getListClassesBySelectedLanguage(selectedLanguage = selectedLanguage),
        selectedItems = selectedClasses,
        onFilterChange = { classes ->
            selectedClasses.clear()
            selectedClasses.addAll(classes)
        }
    )
}