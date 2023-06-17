package com.example.spellsdnd.navigation.favorites.homebrew

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.menuActions.MenuActionsItem
import com.example.spellsdnd.menuActions.ShowMenuActions
import com.example.spellsdnd.navigation.favorites.Homebrew
import com.example.spellsdnd.navigation.favorites.saveFavoritesToPrefs
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.spell.CardState
import com.example.spellsdnd.navigation.spell.ShowCardSide
import com.example.spellsdnd.ui.theme.SpellDndTheme

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

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            ShowCardSide(cardState, spellDetail, showMenu)
        }
    }
}





fun addHomebrewItem(spellDetail: SpellDetail, context: Context) {
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
fun HomebrewMenuActionsContainer(
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
