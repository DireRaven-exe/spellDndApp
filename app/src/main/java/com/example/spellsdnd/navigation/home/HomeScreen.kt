package com.example.spellsdnd.navigation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController, settingsApp: Settings) {
    val filterText = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    Column(
        modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(
                spellsList.filter { spellDetail ->
                    spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                            spellDetail.name.contains(filterText.value, ignoreCase = true)
                }
            ) { spellDetail ->
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

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            FiltersScreen(
                settingsApp = settingsApp,
                onApplyFilter = { _ -> },
                sheetState = sheetState
            )
        },
        content = {
            // Screen content
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
                }
            ) {
                Column(
                    modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)
                ) {
                    TextFieldBox(filterText = filterText)
                    LazyColumn {
                        items(
                            spellsList.filter { spellDetail ->
                                spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                                        spellDetail.name.contains(filterText.value, ignoreCase = true)
                            }
                        ) { spellDetail ->
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
    )
}
