package com.example.spellsdnd.navigation.favorites.homebrew

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellCreationState
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewItems
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.ui.theme.SpellDndTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomebrewScreen(
    spellCreationState: SpellCreationState,
    settingsApp: Settings,
    navController: NavController,
    context: Context
) {
    val filterText = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = SpellDndTheme.colors.primaryBackground,
        sheetContent = { CreateHomebrewItem(sheetState, spellCreationState, settingsApp) { spellDetail ->
            addHomebrewItem(spellDetail, context)
            }
        },
        content = {
            Scaffold(
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(id = R.string.add_card),
                                color = SpellDndTheme.colors.buttonContentColor
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "",
                                tint = SpellDndTheme.colors.buttonContentColor
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
                LazyColumn(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
                    items(getHomebrewItems()[0].filter { spellDetail ->
                        spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                                spellDetail.name.contains(filterText.value, ignoreCase = true)
                    }
                    ) { spellDetail ->
                        HomebrewItems(
                            navController = navController,
                            spellDetail = spellDetail,
                            isPinned = false
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
    )
}

