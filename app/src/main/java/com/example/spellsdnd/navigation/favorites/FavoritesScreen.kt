package com.example.spellsdnd.navigation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.navigation.favorites.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.TextFieldBox

/**
 * Метод, который отрисовывает поле для поиска/фильтра
 * избранных заклинаний и сами карточки заклинаний
 */
@Composable
fun FavoritesScreen(navController: NavController, settingsApp: Settings) {
    val filterText = remember { mutableStateOf("") }

    Column (
        modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(getListBySelectedLanguage(settingsApp.selectedLanguage).filter { spellDetail ->
                spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                        spellDetail.name.contains(filterText.value, ignoreCase = true)
            }
            ) { spellDetail ->
                SpellCardScreen(
                    settingsApp = settingsApp,
                    navController = navController,
                    spellDetail = spellDetail,
                    isPinnedAndIsFavoriteScreen = Pair(false, true)
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