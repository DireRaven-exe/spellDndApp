@file:OptIn(ExperimentalMaterialApi::class)

package com.example.spellsdnd.navigation.filter

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.favorites.homebrew.CreateMultiSelectRowScrollableButtons
import com.example.spellsdnd.navigation.filter.DataForFilter.getListClassesBySelectedLanguage
import com.example.spellsdnd.navigation.filter.DataForFilter.getListLevelBySelectedLanguage
import com.example.spellsdnd.navigation.filter.DataForFilter.getListSchoolsBySelectedLanguage
import com.example.spellsdnd.navigation.settings.CreateSpacerWithDivider
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FiltersScreen(
    filterValues: FilterData,
    settingsApp: Settings,
    onApplyFilter: (List<SpellDetail>) -> Unit,
    sheetState: ModalBottomSheetState
) {
    val selectedSchools = remember { mutableStateOf("") }
    val selectedClasses = remember { mutableStateOf("") }
    val selectedLevels = remember { mutableStateOf("") }
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
                    CreateMultiSelectRowScrollableButtons(
                        label = stringResource(id = R.string.level),
                        selectedValues = selectedLevels,
                        options = getListLevelBySelectedLanguage(settingsApp.selectedLanguage)
                    )
                    CreateSpacerWithDivider()
                }
                item {
                    CreateMultiSelectRowScrollableButtons(
                        label = stringResource(id = R.string.school),
                        selectedValues = selectedSchools,
                        options = getListSchoolsBySelectedLanguage(settingsApp.selectedLanguage)
                    )
                    CreateSpacerWithDivider()
                }
                item {
                    CreateMultiSelectRowScrollableButtons(
                        label = stringResource(id = R.string.dnd_class),
                        selectedValues = selectedClasses,
                        options = getListClassesBySelectedLanguage(settingsApp.selectedLanguage)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        CancelFiltersButton(filterValues, selectedSchools, selectedClasses, selectedLevels)

                        ApplyFiltersButton(
                            sheetState,
                            filterValues,
                            selectedSchools.value,
                            selectedClasses.value,
                            selectedLevels.value,
                            onApplyFilter
                        )
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
private fun CancelFiltersButton(
    filterValues: FilterData,
    selectedSchools: MutableState<String>,
    selectedClasses: MutableState<String>,
    selectedLevels: MutableState<String>
) {
    Button(
        onClick = {
            // Восстанавливаем исходные данные списка заклинаний
            spellsList.clear()
            spellsList.addAll(originalSpellsList)
            // Сбрасываем выбранные значения фильтров
            filterValues.selectedSchools.value = ""
            filterValues.selectedClasses.value = ""
            filterValues.selectedLevels.value = ""
            selectedSchools.value = ""
            selectedClasses.value = ""
            selectedLevels.value = ""
        },
        modifier = Modifier.padding(top = 16.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.primaryBackground,
            contentColor = SpellDndTheme.colors.primaryText
        ),
        shape = RoundedCornerShape(12.dp)
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
    filterValues: FilterData,
    selectedSchools: String,
    selectedClasses: String,
    selectedLevels: String,
    onApplyFilter: (List<SpellDetail>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        onClick = {
            // Фильтрация списка заклинаний по выбранным параметрам
            spellsList.removeAll { spell ->
                !changeListByFilters(
                    spell,
                    selectedSchools,
                    selectedClasses,
                    selectedLevels
                )
            }
            onApplyFilter(spellsList) // Вызов обработчика применения фильтров

            coroutineScope.launch {
                sheetState.hide()
            }
        },
        modifier = Modifier.padding(top = 16.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = SpellDndTheme.colors.primaryBackground,
            contentColor = SpellDndTheme.colors.primaryText
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(stringResource(id = R.string.accept))
    }
}

private fun changeListByFilters(
    spell: SpellDetail,
    selectedSchools: String,
    selectedClasses: String,
    selectedLevels: String
): Boolean {
    val schoolFilter = selectedSchools.isEmpty() || selectedSchools.contains(spell.school.lowercase())
    val classFilter = spell.dnd_class.split(", ").any { dndClass ->
        dndClass in selectedClasses || selectedClasses.isEmpty()
    }
    val levelFilter = selectedLevels.isEmpty() || selectedLevels.contains(spell.level)
    return schoolFilter && classFilter && levelFilter
}
