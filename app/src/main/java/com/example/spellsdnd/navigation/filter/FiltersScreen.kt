@file:OptIn(ExperimentalMaterialApi::class)

package com.example.spellsdnd.navigation.filter

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.filter.DataForFilter.spellLevels
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FiltersScreen(
    settingsApp: Settings,
    onApplyFilter: (List<SpellDetail>) -> Unit,
    sheetState: ModalBottomSheetState
) {

    val selectedSchools = remember { mutableSetOf<String>() }
    val selectedClasses = remember { mutableSetOf<String>() }
    val selectedLevels = remember { mutableSetOf<Int>() }

    val filterData = remember {
        FilterData(
            selectedSchools = mutableSetOf(),
            selectedClasses = mutableSetOf(),
            selectedLevels = mutableSetOf()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.filters),
                        color = SpellDndTheme.colors.primaryText,
                        fontWeight = SpellDndTheme.typography.heading.fontWeight,
                        fontSize = SpellDndTheme.typography.heading.fontSize
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp, // Установка нулевой высоты тени
            )
        },
        backgroundColor = SpellDndTheme.colors.primaryBackground
    ) {
        Card(
            modifier = Modifier
                .background(SpellDndTheme.colors.primaryBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(SpellDndTheme.colors.secondaryBackground)
            ) {
                item {
                    FilterPanel(
                        label = stringResource(id = R.string.level),
                        items = spellLevels,
                        selectedItems = selectedLevels.map { it.toString() }.toMutableSet(),
                        onFilterChange = { selectedItems ->
                            selectedLevels.clear()
                            selectedLevels.addAll(selectedItems.mapNotNull { it.toIntOrNull() })
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))




                }
                item {
                    FilterPanel(
                        label = stringResource(id = R.string.school),
                        items = DataForFilter.getListSchoolsBySelectedLanguage(settingsApp.selectedLanguage),
                        selectedItems = selectedSchools,
                        onFilterChange = { schools ->
                            selectedSchools.clear()
                            selectedSchools.addAll(schools)
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    FilterPanel(
                        label = stringResource(id = R.string.dnd_class),
                        items = DataForFilter.getListClassesBySelectedLanguage(settingsApp.selectedLanguage),
                        selectedItems = selectedClasses,
                        onFilterChange = { classes ->
                            selectedClasses.clear()
                            selectedClasses.addAll(classes)
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ApplyFiltersButton(
                            sheetState,
                            selectedSchools,
                            selectedClasses,
                            selectedLevels,
                            onApplyFilter
                        )

                        CancelFiltersButton()
                    }
                }
            }
        }
    }
}

/**
 * Кнопка отмены фильтров
 */
@Composable
private fun CancelFiltersButton() {
    TextButton(
        // Кнопка отмены фильтров
        onClick = {
            spellsList.clear()
            spellsList.addAll(originalSpellsList)
        },
        modifier = Modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.secondaryBackground,
            contentColor = SpellDndTheme.colors.primaryText
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
    sheetState: ModalBottomSheetState,
    selectedSchools: MutableSet<String>,
    selectedClasses: MutableSet<String>,
    selectedLevels: MutableSet<Int>,
    onApplyFilter: (List<SpellDetail>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    TextButton(
        // Кнопка применения фильтров
        onClick = {
            // Фильтрация списка заклинаний по выбранным параметрам
            spellsList.removeAll { spell ->
                !changeListByFilters(selectedSchools, selectedClasses, selectedLevels).contains(spell)
            }
            onApplyFilter(spellsList) // Вызов обработчика применения фильтров

            coroutineScope.launch {
                sheetState.hide()
            }
        },
        modifier = Modifier.padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.secondaryBackground,
            contentColor = SpellDndTheme.colors.primaryText,
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

