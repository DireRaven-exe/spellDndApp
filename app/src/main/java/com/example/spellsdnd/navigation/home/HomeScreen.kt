package com.example.spellsdnd.navigation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import com.example.spellsdnd.navigation.favorites.Favorites.saveFavoritesToPrefs
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.utils.MutableListManager.spellsList
import com.example.spellsdnd.utils.TextFieldBox
import com.example.spellsdnd.utils.Utils.isVisibleSpell

/**
 * Метод, который отрисовывает
 * поле для поиска/фильтра заклинаний и сами карточки заклинаний (TextField, SpellCard)
 * @param spellsList - список заклинаний
 */
@Composable
fun HomeScreen(navController: NavController, selectedLanguage: MutableState<String>) {
    val filterText = remember { mutableStateOf("") }
    val context = LocalContext.current

    val isPinSpell = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(
                spellsList.filter { spellDetail -> // Фильтрация карточек
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
                            if (getListBySelectedLanguage(selectedLanguage).contains(spellDetail)) {
                                // элемент уже есть в списке, вывести сообщение об ошибке
                                Toast.makeText(
                                    context,
                                    R.string.already_added_this_spell,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // добавляем элемент и выводим сообщение об успехе
                                getListBySelectedLanguage(selectedLanguage).add(spellDetail)
                                saveFavoritesToPrefs(context)
                                Toast.makeText(
                                    context,
                                    R.string.successfully_added,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_favorites_white),
                            contentDescription = "Add to favorites",
                            tint = DarkBlueColorTheme.screenInactiveColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    IconButton(
                        onClick = {
                            isVisibleSpell.value = true
                            navController.navigate(Screens.Spell(spellDetail.slug).route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_lock),
                            contentDescription = "Pin Spell Card",
                            tint = DarkBlueColorTheme.screenInactiveColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                SpellCardScreen(spellDetail = spellDetail)
                Divider(modifier = Modifier.height(6.dp))
            }
        }
    }
}
