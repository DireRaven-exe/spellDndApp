package com.example.spellsdnd.navigation.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.navigation.favorites.Favorites
import com.example.spellsdnd.navigation.favorites.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.utils.MutableListManager.spellsList
import com.example.spellsdnd.utils.TextFieldBox

/**
 * Метод, который отрисовывает
 * поле для поиска/фильтра заклинаний и сами карточки заклинаний (TextField, SpellCard)
 * @param spellsList - список заклинаний
 */
@Composable
fun HomeScreen(navController: NavController, selectedLanguage: MutableState<String>) {
    val filterText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)
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
                    selectedLanguage = selectedLanguage,
                    navController = navController,
                    spellDetail = spellDetail,
                    isPinnedAndIsFavoriteScreen = Pair(false, false)
                )
                Divider(modifier = Modifier.height(6.dp))
            }
        }
    }
}
