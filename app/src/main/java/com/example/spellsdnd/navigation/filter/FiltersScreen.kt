package com.example.spellsdnd.navigation.filter

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList


@SuppressLint("MutableCollectionMutableState")
@Composable
fun FiltersScreen(selectedLanguage: MutableState<String>, onApplyFilter: (List<SpellDetail>) -> Unit ) {

    // Получаем значения переменных из SharedPreferences
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
                SpellSchoolFilter(selectedLanguage, selectedSchools = selectedSchools)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Column {// Панель фильтрации по классам ДнД
                ClassFilter(selectedLanguage, selectedClasses = selectedClasses)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                ApplyFiltersButton(selectedSchools, selectedClasses, selectedLevels, onApplyFilter)
                CancelFiltersButton()
            }
        }
    }
}

/**
 * Кнопка отмены фильтров
 */
@Composable
private fun CancelFiltersButton() {
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
 *
 * @param selectedClasses - выбранные классы
 * @param selectedLevels - выбранные уровни
 * @param selectedSchools - выбранные школы
 * @param onApplyFilter - фильтрация
 */
@Composable
private fun ApplyFiltersButton(
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
                !changeListByFilters(selectedSchools, selectedClasses, selectedLevels).contains(spell)
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
private fun changeListByFilters(
    selectedSchools: MutableSet<String>,
    selectedClasses: MutableSet<String>,
    selectedLevels: MutableSet<Int>
): List<SpellDetail> {
    val filteredList = spellsList.filter { spell ->
        val schoolFilter = selectedSchools.isEmpty() ||
                selectedSchools.contains(spell.school.lowercase())
        val classFilter = spell.dnd_class.split(", ").map { className ->
            className
        }.any { dndClass ->
            dndClass in selectedClasses || selectedClasses.isEmpty()
        }
        val levelFilter = selectedLevels.isEmpty() ||
                selectedLevels.contains(spell.level_int)
        schoolFilter && classFilter && levelFilter
    }
    return filteredList
}

