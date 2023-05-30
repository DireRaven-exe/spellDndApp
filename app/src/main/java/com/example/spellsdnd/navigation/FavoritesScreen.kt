package com.example.spellsdnd.navigation

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.utils.DarkBlueColorTheme
import com.example.spellsdnd.utils.Favorites
import com.example.spellsdnd.utils.Favorites.getFavoritesSpells
import com.example.spellsdnd.utils.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.utils.Favorites.loadFavoritesFromPrefs
import com.example.spellsdnd.utils.Favorites.saveFavoritesToPrefs
import com.example.spellsdnd.utils.Favorites.setListBySelectedLanguage
import com.example.spellsdnd.utils.TextFieldBox


@Composable
fun FavoritesScreen(selectedLanguage: MutableState<String>) {
    val filterText = remember { mutableStateOf("") }

    val context = LocalContext.current
    loadFavoritesFromPrefs(context)
//    savedFavorites.forEach { spellList ->
//        spellList.forEach { spellDetail ->
//            if (!getListBySelectedLanguage(selectedLanguage).contains(spellDetail)) {
//                getListBySelectedLanguage(selectedLanguage).add(spellDetail)
//            }
//        }
//    }

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
                            val selectedList = getListBySelectedLanguage(selectedLanguage)
                            val updatedFavorites = selectedList.toMutableList()
                            updatedFavorites.remove(spellDetail)
                            setListBySelectedLanguage(selectedLanguage, updatedFavorites)
                            saveFavoritesToPrefs(context)
                        }
                    ) {
//                    IconButton(
//                        onClick = {
//                            getListBySelectedLanguage(selectedLanguage).remove(spellDetail)
//                            saveFavoritesToPrefs(context)
//                        //saveFavoritesToPrefs(context, getFavoritesSpells())
//                        }
//                    ) {
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