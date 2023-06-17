package com.example.spellsdnd.navigation.spell

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.menuActions.MenuActionsItem
import com.example.spellsdnd.menuActions.ShowMenuActions
import com.example.spellsdnd.navigation.favorites.Favorites.getFavoritesPrefsKet
import com.example.spellsdnd.navigation.favorites.Favorites.getFavoritesSpells
import com.example.spellsdnd.navigation.favorites.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.navigation.favorites.Favorites.setListBySelectedLanguage
import com.example.spellsdnd.navigation.favorites.saveFavoritesToPrefs
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.spell.card.InfoCardSide
import com.example.spellsdnd.navigation.spell.card.MainCardSide
import com.example.spellsdnd.ui.theme.SpellDndTheme

/**
 * Перечисление, обозначающее сторону карты
 */
enum class CardState {
    Front, Back
}

/**
 * Метод, который отвечает за навигацию
 * отображает либо основной экран (MainScreen)
 * либо экран с информацией о заклинании (InfoScreen)
 * @param spellDetail - информация о заклинании
 */
@Composable
fun SpellCardScreen(
    settingsApp: Settings,
    navController: NavController,
    spellDetail: SpellDetail,
    isPinnedAndIsFavoriteScreen: Pair<Boolean, Boolean>
) {
    val showMenu = remember { mutableStateOf(false) }
    val cardState = remember { mutableStateOf(CardState.Front) }

    MenuActionsContainer(
        showMenu,
        navController,
        spellDetail,
        settingsApp,
        isPinnedAndIsFavoriteScreen
    )
    Column(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
        if (isPinnedAndIsFavoriteScreen.first) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_pin_card),
                        color = SpellDndTheme.colors.primaryText,
                        fontWeight = SpellDndTheme.typography.heading.fontWeight,
                        fontSize = SpellDndTheme.typography.heading.fontSize
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(
                            if (getListBySelectedLanguage(settingsApp.selectedLanguage).contains(spellDetail)) {
                                R.drawable.icon_favorites_added
                            } else { R.drawable.icon_favorites_not_added } ),
                        contentDescription = "Add to favorites",
                        tint = SpellDndTheme.colors.primaryIcon,
                        modifier = Modifier.size(24.dp).padding(end = 0.dp),
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                elevation = 0.dp
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            if (!isPinnedAndIsFavoriteScreen.first) {
                FavoriteIconStateBox(settingsApp, spellDetail)
            }
            ShowCardSide(cardState, spellDetail, showMenu)
        }
    }
}

@Composable
fun ShowCardSide(
    cardState: MutableState<CardState>,
    spellDetail: SpellDetail,
    showMenu: MutableState<Boolean>
) {
    when (cardState.value) {
        CardState.Front -> {
            MainCardSide(
                spellDetail = spellDetail,
                onClick = { cardState.value = CardState.Back },
                onLongClick = { showMenu.value = true }
            )
        }

        CardState.Back -> {
            InfoCardSide(
                spellDetail = spellDetail,
                onClick = { cardState.value = CardState.Front },
                onLongClick = { showMenu.value = true }
            )
        }
    }
}

/**
 * Composable функция для отображения иконки "Избранное".
 */
@Composable
private fun FavoriteIconStateBox(
    settingsApp: Settings,
    spellDetail: SpellDetail,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(
                if (getListBySelectedLanguage(settingsApp.selectedLanguage).contains(spellDetail)) {
                        R.drawable.icon_favorites_added
                } else { R.drawable.icon_favorites_not_added } ),
            contentDescription = "Add to favorites",
            tint = SpellDndTheme.colors.primaryIcon,
            modifier = Modifier.size(24.dp),
        )
    }
}

/**
 * Composable функция для заполнения и отображения меню действий.
 */
@Composable
private fun MenuActionsContainer(
    showMenu: MutableState<Boolean>,
    navController: NavController,
    spellDetail: SpellDetail,
    settingsApp: Settings,
    isPinnedAndIsFavoriteScreen: Pair<Boolean, Boolean>
) {
    val context = LocalContext.current
    val menuActionsItems = mutableListOf<MenuActionsItem>()

    if (showMenu.value) {
        if (isPinnedAndIsFavoriteScreen.first) {
            menuActionsItems.clear()
            menuActionsItems.add(
                MenuActionsItem(
                    title = stringResource(id = R.string.unpin_card),
                    icon = R.drawable.icon_unpin,
                    action = {
                        val targetRoute = if (!isPinnedAndIsFavoriteScreen.second) {
                            Screens.Home.route
                        } else {
                            Screens.Favorites.route
                        }

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
        }
        else {
            menuActionsItems.clear()
            menuActionsItems.add(
                MenuActionsItem(
                    title = stringResource(id = R.string.pin_card),
                    icon = R.drawable.icon_pin,
                    action = {
                        if (!isPinnedAndIsFavoriteScreen.second) {
                            navController.navigate(Screens.Spell.enterSlug(spellDetail.slug)) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
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
                    }
                )
            )
        }

        menuActionsItems.add(
            MenuActionsItem(
                title = if (getListBySelectedLanguage(settingsApp.selectedLanguage).contains(spellDetail)) {
                    stringResource(id = R.string.delete_from_favorites)
                } else {
                    stringResource(id = R.string.add_to_favorites)
                },
                icon = if (getListBySelectedLanguage(settingsApp.selectedLanguage).contains(spellDetail)) {
                    R.drawable.icon_favorites_added
                } else {
                    R.drawable.icon_favorites_not_added
                },
                action = {
                    if (getListBySelectedLanguage(settingsApp.selectedLanguage).contains(spellDetail)) {
                        val selectedList = getListBySelectedLanguage(settingsApp.selectedLanguage)
                        val updatedFavorites = selectedList.toMutableList()
                        updatedFavorites.remove(spellDetail)
                        setListBySelectedLanguage(settingsApp.selectedLanguage, updatedFavorites)
                        saveFavoritesToPrefs(context, getFavoritesSpells(), getFavoritesPrefsKet())
                        Toast.makeText(
                            context,
                            R.string.successfully_deleted,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val selectedList = getListBySelectedLanguage(settingsApp.selectedLanguage)
                        val updatedFavorites = selectedList.toMutableList()
                        updatedFavorites.add(spellDetail)
                        setListBySelectedLanguage(settingsApp.selectedLanguage, updatedFavorites)
                        saveFavoritesToPrefs(context, getFavoritesSpells(), getFavoritesPrefsKet())
                        Toast.makeText(
                            context,
                            R.string.successfully_added,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

