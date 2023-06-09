package com.example.spellsdnd.navigation.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.navigation.favorites.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.navigation.favorites.Favorites.loadFavoritesFromPrefs
import com.example.spellsdnd.navigation.favorites.Favorites.saveFavoritesToPrefs
import com.example.spellsdnd.navigation.favorites.Favorites.setListBySelectedLanguage
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.utils.TextFieldBox
import com.example.spellsdnd.utils.Utils


@Composable
fun FavoritesScreen(navController: NavController, selectedLanguage: MutableState<String>) {
    val filterText = remember { mutableStateOf("") }

    val context = LocalContext.current
    loadFavoritesFromPrefs(context)

    Column (
        modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(getListBySelectedLanguage(selectedLanguage).filter { spellDetail ->
                spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                        spellDetail.name.contains(filterText.value, ignoreCase = true)
            }
            ) { spellDetail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            Utils.isVisibleSpell.value = true
                            navController.navigate(Screens.FavoriteSpell(spellDetail.slug, true).route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_lock),
                            contentDescription = "Pin Spell Card",
                            tint = DarkBlueColorTheme.screenInactiveColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            val selectedList = getListBySelectedLanguage(selectedLanguage)
                            val updatedFavorites = selectedList.toMutableList()
                            updatedFavorites.remove(spellDetail)
                            setListBySelectedLanguage(selectedLanguage, updatedFavorites)
                            saveFavoritesToPrefs(context)
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_delete),
                            contentDescription = "Remove from favorites",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                SpellCardScreen(spellDetail = spellDetail)
                Divider(modifier = Modifier.height(6.dp)
                )
            }
        }
    }
}