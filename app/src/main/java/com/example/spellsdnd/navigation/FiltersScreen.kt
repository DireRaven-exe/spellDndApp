@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.spellsdnd.navigation

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.utils.DarkBlueColorTheme
import com.example.spellsdnd.utils.DndClass
import com.example.spellsdnd.utils.SpellSchool
import com.example.spellsdnd.utils.Utils.originalSpellsList
import com.example.spellsdnd.utils.Utils.spellsList
import com.google.android.material.shape.CutCornerTreatment
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
                Button( // Кнопка применения фильтров
                    onClick = {
                        // Фильтрация списка заклинаний по выбранным параметрам
                        val filteredList = spellsList.filter { spell ->
                            val schoolFilter = selectedSchools.isEmpty() ||
                                    selectedSchools.contains(spell.school.toLowerCase(Locale.ENGLISH))
                            val classFilter = spell.dnd_class.split(", ").any {
                                it.trim() in selectedClasses || selectedClasses.isEmpty()
                            }
                            val levelFilter = selectedLevels.isEmpty() ||
                                    selectedLevels.contains(spell.level_int)
                            schoolFilter && classFilter && levelFilter
                        }.toSet()
                        spellsList.removeAll { spell -> // Устанавливаем новое значение spellsList
                            !filteredList.contains(spell)
                        }
                        //spellsList.addAll(filteredList)
                        onApplyFilter(spellsList) // Вызов обработчика применения фильтров
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = DarkBlueColorTheme.bottomBarBackgroundColor,
                        contentColor = DarkBlueColorTheme.textColor
                    )
                ) {
                    Text("Use filters")
                }
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
                    Text("Cancel")
                }
            }
        }
    }
}

/**
 * Панель фильтрации по уровню заклинаний
 * @param selectedLevels - множество выбранных элементов.
 */
@Composable
fun SpellLevelsFilter(selectedLevels: MutableSet<Int>) {
    val spellLevels = (0..9).map { it.toString() }

    Filter(
        label = "Level",
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
    val spellSchools = listOf<String>("evocation",
        "conjuration",
        "abjuration",
        "transmutation",
        "enchantment",
        "illusion",
        "necromancy",
        "divination"
    )

    Filter(
        label = "School",
        icon = R.drawable.icon_filter_school,
        items = spellSchools,
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
    val dndClasses = enumValues<DndClass>().map { it.name.lowercase()
        .replaceFirstChar { it -> if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } }.toSet()

    Filter(
        label = "Class",
        icon = R.drawable.icon_filter_class,
        items = dndClasses.toList(),
        selectedItems = selectedClasses,
        onFilterChange = { classes ->
            selectedClasses.clear()
            selectedClasses.addAll(classes)
        }
    )
}







/**
 * Метод, который отображает одно поле для фильтрации по выбранному параметру
 * (Spells, School, Level etc.)
 *
 * @param label: Это текстовое значение, которое будет
 * использоваться в качестве метки для поля ввода.
 *
 * @param items: Это список элементов, из которых пользователь может выбрать.
 *
 * @param selectedItems: Это множество выбранных элементов.
 *
 * @param onFilterChange: Это функция обратного вызова, которая будет
 * вызываться при изменении выбранных элементов.
 */
@Composable
fun Filter(
    label: String,
    items: List<String>,
    icon: Int,
    selectedItems: MutableSet<String>,
    onFilterChange: (List<String>) -> Unit,
) {
    var textState by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Card(modifier = Modifier
        .background(DarkBlueColorTheme.mainBackgroundColor)
        .fillMaxWidth()
        .shadow(15.dp, RoundedCornerShape(6.dp))
        .drawWithContent {
            drawContent()
            drawLine(
                color = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                strokeWidth = 4.dp.toPx(),
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height)
            )
        },
        shape = RoundedCornerShape(4.dp)
        ,
        ) {
        Row(
            modifier = Modifier
                .height(53.dp)
                .background(DarkBlueColorTheme.mainBackgroundColor)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .background(DarkBlueColorTheme.mainBackgroundColor)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Expand $label menu",
                    modifier = Modifier.size(36.dp)
                    ,
                )
            }
            TextField(
                value = textState,
                onValueChange = {
                    textState = it
                    selectedItems.clear()
                    selectedItems.addAll(
                        textState.split(",").map { it -> it.trim() })
                    onFilterChange(selectedItems.toList()) },
                label = { Text(label, color = DarkBlueColorTheme.textFieldTitleColor) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                        isKeyboardVisible.value = true
                    }
                    .onFocusChanged { it ->
                        isKeyboardVisible.value = it.isFocused
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = DarkBlueColorTheme.mainBackgroundColor,
                    textColor = DarkBlueColorTheme.textColor,
                    cursorColor = DarkBlueColorTheme.textFieldCursorColor,
                    focusedIndicatorColor = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                    unfocusedIndicatorColor = DarkBlueColorTheme.textFieldUnfocusedIndicatorColor
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        isKeyboardVisible.value = false
                        focusManager.clearFocus()
                    }
                )
            )
            if (isKeyboardVisible.value) {
                TextButton( //Кнопка отмены
                    onClick = {
                        selectedItems.clear()
                        keyboardController?.hide()
                        isKeyboardVisible.value = false
                        focusManager.clearFocus() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = DarkBlueColorTheme.textButtonColor,
                        disabledContentColor = Color.Gray,
                        disabledBackgroundColor = Color.LightGray,
                    ),
                ) {
                    Text(text = "Cancel")
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(DarkBlueColorTheme.mainBackgroundColor)
                .border(
                    2.dp,
                    color = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                    shape = RoundedCornerShape(0.dp)
                )
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        if (selectedItems.contains(item)) {
                            selectedItems.remove(item)
                        } else {
                            selectedItems.add(item)
                        }
                        textState = selectedItems.sorted().joinToString(", ")
                        onFilterChange(selectedItems.toList())
                    }
                ) {
                    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
                        Checkbox(
                            checked = item in selectedItems,
                            onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        uncheckedColor = DarkBlueColorTheme.textFieldFocusedIndicatorColor )
                        )
                        Text(
                            text = item,
                            modifier = Modifier.padding(start = 10.dp),
                            color = DarkBlueColorTheme.textColor
                        )
                    }
                }
            }
        }
    }
}