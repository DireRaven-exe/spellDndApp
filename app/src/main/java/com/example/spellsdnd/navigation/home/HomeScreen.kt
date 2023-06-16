package com.example.spellsdnd.navigation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.navigation.filter.FiltersScreen
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.MutableListManager.spellsList
import com.example.spellsdnd.utils.TextFieldBox
import kotlinx.coroutines.launch

/**
 * Метод, который отрисовывает
 * поле для поиска/фильтра заклинаний и сами карточки заклинаний (TextField, SpellCard)
 * @param spellsList - список заклинаний
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, settingsApp: Settings) {
    val filterText = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()


    val filteredSpells = remember {
        derivedStateOf {
            spellsList.filter { spellDetail ->
                spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                        spellDetail.name.contains(filterText.value, ignoreCase = true)
            }.sortedBy { it.level_int }
        }
    }

    val levels = remember {
        derivedStateOf {
            val levelsList = filteredSpells.value.map { it.level_int }.distinct().toMutableList()
            levelsList
        }
    }

    val expandedLevels = remember { mutableStateMapOf<Int, Boolean>().apply {
        levels.value.forEach { level ->
            this[level] = true
        }
    } }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            FiltersScreen(
                settingsApp = settingsApp,
                onApplyFilter = { _ -> },
                sheetState = sheetState
            )
        },
        sheetBackgroundColor = SpellDndTheme.colors.primaryBackground,
        content = {
            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(id = R.string.add_filters),
                                color = SpellDndTheme.colors.buttonColor
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "",
                                tint = SpellDndTheme.colors.buttonColor
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        backgroundColor = SpellDndTheme.colors.buttonBackgroundColor
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground
            ) {
                Column(
                    modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)
                ) {
                    TextFieldBox(filterText = filterText)

                    LazyColumn {
                        levels.value.forEach { level ->
                            stickyHeader {
                                SpellLevelHeader(
                                    level = level,
                                    isExpanded = expandedLevels[level] ?: true,
                                    onClick = {
                                        expandedLevels[level] = !(expandedLevels[level] ?: true)
                                    }
                                )
                            }
                            items(
                                items = filteredSpells.value.filter { it.level_int == level },
                                key = { spellDetail -> spellDetail.slug }
                            ) { spellDetail ->
                                if (expandedLevels[level] != false) {
                                    SpellCardScreen(
                                        settingsApp = settingsApp,
                                        navController = navController,
                                        spellDetail = spellDetail,
                                        isPinnedAndIsFavoriteScreen = Pair(false, false)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Divider(
                                        modifier = Modifier.height(6.dp),
                                        color = SpellDndTheme.colors.secondaryBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SpellLevelHeader(level: Int, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            modifier = Modifier
                .background(SpellDndTheme.colors.buttonBackgroundColor, RoundedCornerShape(12.dp)),
            text = if (level == 0) stringResource(R.string.cantrip) else stringResource(R.string.level) + " " + level,
            color = SpellDndTheme.colors.buttonColor,
            fontSize = SpellDndTheme.typography.heading.fontSize,
            textAlign = TextAlign.Center
        )
    }
}