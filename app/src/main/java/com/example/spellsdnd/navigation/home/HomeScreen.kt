package com.example.spellsdnd.navigation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.MutableListManager.spellsList
import com.example.spellsdnd.utils.TextFieldBox

/**
 * Метод, который отрисовывает
 * поле для поиска/фильтра заклинаний и сами карточки заклинаний (TextField, SpellCard)
 * @param spellsList - список заклинаний
 */
@Composable
fun HomeScreen(navController: NavController, settingsApp: Settings) {
    val filterText = remember { mutableStateOf("") }

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
