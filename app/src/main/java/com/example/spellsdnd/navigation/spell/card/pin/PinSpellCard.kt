package com.example.spellsdnd.navigation.spell.card.pin

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.favorites.Favorites
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.spell.CardState
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme

@Composable
fun PinSpellCard(
    context: Context,
    selectedLanguage: MutableState<String>,
    spellDetail: SpellDetail,
    navController: NavController,
    isFavorite: Boolean
) {
    val cardState = remember { mutableStateOf(CardState.Front) }
    Column(
        modifier = Modifier
            .background(DarkBlueColorTheme.mainBackgroundColor)
            .padding(
                top = 0.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Row() {
            IconButton(
                onClick = {
                    if (!isFavorite) {
                        if (Favorites.getListBySelectedLanguage(selectedLanguage)
                                .contains(spellDetail)
                        ) {
                            // элемент уже есть в списке, вывести сообщение об ошибке
                            Toast.makeText(
                                context,
                                R.string.already_added_this_spell,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // добавляем элемент и выводим сообщение об успехе
                            Favorites.getListBySelectedLanguage(selectedLanguage).add(spellDetail)
                            Favorites.saveFavoritesToPrefs(context)
                            Toast.makeText(
                                context,
                                R.string.successfully_added,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val selectedList = Favorites.getListBySelectedLanguage(selectedLanguage)
                        val updatedFavorites = selectedList.toMutableList()
                        updatedFavorites.remove(spellDetail)
                        Favorites.setListBySelectedLanguage(selectedLanguage, updatedFavorites)
                        Favorites.saveFavoritesToPrefs(context)
                    }
                }) {
                Icon(
                    painter = painterResource(if (!isFavorite) R.drawable.icon_favorites_white else R.drawable.icon_delete),
                    contentDescription = "Add to favorites",
                    tint = DarkBlueColorTheme.screenActiveColor,
                    modifier = Modifier.size(24.dp),
                )
                }
                IconButton(
                    onClick = {
                        if(!isFavorite) {
                            navController.navigate(Screens.Home.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = false
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            navController.navigate(Screens.Favorites.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = false
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_unlock),
                        contentDescription = "Unlock Spell Card",
                        tint = DarkBlueColorTheme.screenActiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    when (cardState.value) {
        CardState.Front -> {
            PinSpellCardSideMain(
                spellDetail = spellDetail,
                onClick = { cardState.value = CardState.Back },
            )
        }
        CardState.Back -> {
            PinSpellCardSideInfo(
                spellDetail = spellDetail,
                onClick = { cardState.value = CardState.Front },
            )
        }
    }
}