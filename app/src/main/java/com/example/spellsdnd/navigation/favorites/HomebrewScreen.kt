package com.example.spellsdnd.navigation.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.menuActions.MenuActionsItem
import com.example.spellsdnd.menuActions.ShowMenuActions
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewItems
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.spell.CardState
import com.example.spellsdnd.navigation.spell.ShowCardSide
import com.example.spellsdnd.ui.theme.SpellDndTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomebrewScreen(
    navController: NavController,
    context: Context
) {
    val filterText = remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = SpellDndTheme.colors.primaryBackground,
        sheetContent = { CreateHomebrewItem(onCreate = { spellDetail -> addHomebrewItem(spellDetail, context) }) },
        content = {
            Scaffold(
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = {
                            Text(
                                text = stringResource(id = R.string.add_card),
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
                LazyColumn(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
                    items(Homebrew.getHomebrewItems()[0].filter { spellDetail ->
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


@Composable
fun HomebrewItems(
    navController: NavController,
    spellDetail: SpellDetail,
    isPinned: Boolean
) {
    val showMenu = remember { mutableStateOf(false) }
    val cardState = remember { mutableStateOf(CardState.Front) }

    HomebrewMenuActionsContainer(
        showMenu,
        navController,
        spellDetail,
        isPinned
    )
    Column(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
        if (isPinned) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_pin_card),
                        color = SpellDndTheme.colors.primaryText,
                        fontWeight = SpellDndTheme.typography.heading.fontWeight,
                        fontSize = SpellDndTheme.typography.heading.fontSize
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                elevation = 0.dp
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            ShowCardSide(cardState, spellDetail, showMenu)
        }
    }
}

private fun addHomebrewItem(spellDetail: SpellDetail, context: Context) {
    val selectedList = Homebrew.getHomebrewItems()[0]
    val updatedFavorites = selectedList.toMutableList()
    updatedFavorites.add(spellDetail)
    Homebrew.setHomebrewItems(updatedFavorites)
    saveFavoritesToPrefs(context, Homebrew.getHomebrewItems(), Homebrew.getHomebrewPrefsKey())
    Toast.makeText(
        context,
        R.string.successfully_added,
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
private fun HomebrewMenuActionsContainer(
    showMenu: MutableState<Boolean>,
    navController: NavController,
    spellDetail: SpellDetail,
    isPinned: Boolean
) {
    val context = LocalContext.current
    val menuActionsItems = mutableListOf<MenuActionsItem>()

    if (showMenu.value) {
        if (isPinned) {
            menuActionsItems.clear()
            menuActionsItems.add(
                MenuActionsItem(
                    title = stringResource(id = R.string.unpin_card),
                    icon = R.drawable.icon_unpin,
                    action = {
                        val targetRoute = Screens.Favorites.route

                        navController.navigate(targetRoute) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = false
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ),
            )

        } else {
            menuActionsItems.clear()
            menuActionsItems.add(
                MenuActionsItem(
                    title = stringResource(id = R.string.pin_card),
                    icon = R.drawable.icon_pin,
                    action = {
                        navController.navigate(Screens.FavoriteSpell.enterSlug(spellDetail.slug)) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            )
        }

        menuActionsItems.add(
            MenuActionsItem(
                title = stringResource(id = R.string.delete_from_favorites),
                icon = R.drawable.icon_delete,
                action = {
                    val selectedList = Homebrew.getHomebrewItems()[0]
                    val updatedFavorites = selectedList.toMutableList()
                    updatedFavorites.remove(spellDetail)
                    Homebrew.setHomebrewItems(updatedFavorites)
                    saveFavoritesToPrefs(context, Homebrew.getHomebrewItems(), Homebrew.getHomebrewPrefsKey())
                    Toast.makeText(
                        context,
                        R.string.successfully_deleted,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        )

        ShowMenuActions(
            showMenu = showMenu,
            itemCount = menuActionsItems.size,
            menuItems = menuActionsItems
        )
    }
}

// Локальное состояние для хранения данных формы



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CreateHomebrewItem(onCreate: (SpellDetail) -> Unit) {
    val lastSlug = getHomebrewItems()[0].lastOrNull()?.slug ?: ""
    val generatedSlug = "homebrew-spell-" + (lastSlug.toIntOrNull() ?: (0 + 1))
    val slug = remember { mutableStateOf(generatedSlug) }
    val name = remember { mutableStateOf("") }
    val desc = remember { mutableStateOf("") }
    val higherLevel = remember { mutableStateOf("") }
    val page = remember { mutableStateOf("") }
    val range = remember { mutableStateOf("") }
    val components = remember { mutableStateOf("") }
    val material = remember { mutableStateOf("") }
    val ritual = remember { mutableStateOf("") }
    val duration = remember { mutableStateOf("") }
    val concentration = remember { mutableStateOf("") }
    val castingTime = remember { mutableStateOf("") }
    val level = remember { mutableStateOf("") }
    val levelInt = remember { mutableStateOf(0) }
    val school = remember { mutableStateOf("") }
    val dndClass = remember { mutableStateOf("") }
    val archetype = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.create_card),
                        color = SpellDndTheme.colors.primaryText,
                        fontWeight = SpellDndTheme.typography.heading.fontWeight,
                        fontSize = SpellDndTheme.typography.heading.fontSize
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp
            )
        },
        backgroundColor = SpellDndTheme.colors.primaryBackground
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                CreateTextField("Name", name)
                CreateTextField("Description", desc)
                CreateTextField("Higher level", higherLevel)
                CreateTextField("Page", page)
                CreateTextField("Range", range)
                CreateTextField("Components", components)
                CreateTextField("Material", material)
                CreateTextField("Ritual", ritual)
                CreateTextField("Duration", duration)
                CreateTextField("Concentration", concentration)
                CreateTextField("Casting time", castingTime)

                CreateDropdownField("Level", level, levelInt, listOf("Cantrip", "1", "2", "3", "4", "5", "6", "7", "8", "9"))
                CreateDropdownField("School", school, levelInt, listOf("School 1", "School 2", "School 3")) // Замените значениями своей логики
                CreateTextField("Dnd class", dndClass)
                CreateTextField("Archetype", archetype)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { /* Обработчик отмены создания карточки */ }) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        val spellDetail = SpellDetail(
                            slug = slug.value,
                            name = name.value,
                            desc = desc.value,
                            higher_level = higherLevel.value,
                            page = page.value,
                            range = range.value,
                            components = components.value,
                            material = material.value,
                            ritual = ritual.value,
                            duration = duration.value,
                            concentration = concentration.value,
                            casting_time = castingTime.value,
                            level = level.value,
                            level_int = levelInt.value,
                            school = school.value,
                            dnd_class = dndClass.value,
                            archetype = archetype.value,
                            circles = "",
                            document__slug = "",
                            document__title = "",
                            document__license_url = ""
                        )
                        // Вызов функции обратного вызова с созданным экземпляром SpellDetail
                        onCreate(spellDetail)
                    }) {
                        Text("Accept")
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateTextField(label: String, valueState: MutableState<String>) {
    TextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(label) }
    )
}

@Composable
private fun CreateDropdownField(
    label: String,
    valueState: MutableState<String>,
    intValueState: MutableState<Int>,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            modifier = Modifier
                .background(
                    SpellDndTheme.colors.primaryBackground,
                    RoundedCornerShape(12.dp)
                )
                .clickable { expanded = true },
            text = valueState.value,
            color = SpellDndTheme.colors.primaryText,
            fontSize = SpellDndTheme.typography.body.fontSize,
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    valueState.value = option
                    intValueState.value = options.indexOf(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}
