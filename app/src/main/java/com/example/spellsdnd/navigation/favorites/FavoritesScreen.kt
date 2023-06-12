package com.example.spellsdnd.navigation.favorites

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

/**
 * Метод, который отрисовывает поле для поиска/фильтра
 * избранных заклинаний и сами карточки заклинаний
 */
@Composable
fun FavoritesScreen(navController: NavController, selectedLanguage: MutableState<String>) {
    val filterText = remember { mutableStateOf("") }

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
                SpellCardScreen(
                    selectedLanguage = selectedLanguage,
                    navController = navController,
                    spellDetail = spellDetail,
                    isPinnedAndIsFavoriteScreen = Pair(false, true)
                )
                Divider(modifier = Modifier.height(6.dp)
                )
            }
        }
    }
}

//    val context = LocalContext.current
//    loadFavoritesFromPrefs(context)