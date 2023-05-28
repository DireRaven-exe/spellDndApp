package com.example.spellsdnd.navigation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.utils.DarkBlueColorTheme
import com.example.spellsdnd.utils.TextFieldBox
import com.example.spellsdnd.utils.Utils.spellsFavoritesList
import com.example.spellsdnd.utils.Utils.spellsList

/**
 * Метод, который отрисовывает
 * поле для поиска/фильтра заклинаний и сами карточки заклинаний (TextField, SpellCard)
 * @param spellsList - список заклинаний
 */
@Composable
fun HomeScreen() {
    val filterText = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column (
        modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(
                spellsList.filter { spellDetail -> // Фильтрация карточек
                    spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                    spellDetail.name.contains(filterText.value, ignoreCase = true)
            }) { spellDetail ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            if (spellsFavoritesList.contains(spellDetail)) {
                                // элемент уже есть в списке, вывести сообщение об ошибке
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.already_added_this_spell),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // добавляем элемент и выводим сообщение об успехе
                                spellsFavoritesList.add(spellDetail)
                                saveFavoritesToPrefs(context, spellsFavoritesList)
                                Toast.makeText(context, context.getString(R.string.successfully_added), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_favorites_white),
                            contentDescription = "Add to favorites",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                SpellCardScreen(spellDetail = spellDetail)
                Divider(
                    modifier = Modifier.height(6.dp)
                )
            }
        }
    }
}

