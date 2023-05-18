package com.example.spellsdnd.navigation

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.data.SpellDetail
import com.google.gson.Gson
import com.example.spellsdnd.R
import com.example.spellsdnd.utils.DarkBlueColorTheme
import com.example.spellsdnd.utils.TextFieldBox
import com.example.spellsdnd.utils.Utils.spellsFavoritesList


val FAVORITES_PREFS_KEY = "favorites_prefs_key"
// Функция сохранения списка избранных заклинаний в файл настроек
fun saveFavoritesToPrefs(context: Context, favoritesList: List<SpellDetail>) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        FAVORITES_PREFS_KEY, Context.MODE_PRIVATE
    )
    with(sharedPrefs.edit()) {
        val serializedFavorites = Gson().toJson(favoritesList)
        putString(FAVORITES_PREFS_KEY, serializedFavorites)
        apply()
    }
}

// Функция загрузки списка избранных заклинаний из файла настроек
fun loadFavoritesFromPrefs(context: Context): List<SpellDetail> {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        FAVORITES_PREFS_KEY, Context.MODE_PRIVATE
    )
    val serializedFavorites = sharedPrefs.getString(FAVORITES_PREFS_KEY, null)
    return if (serializedFavorites != null) {
        try {
            Gson().fromJson(serializedFavorites, Array<SpellDetail>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    } else {
        emptyList()
    }
}


@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val savedFavorites = loadFavoritesFromPrefs(context)
    val filterText = remember { mutableStateOf("") }
    savedFavorites.forEach { spellDetail ->
        if (!spellsFavoritesList.contains(spellDetail)) {
            spellsFavoritesList.add(spellDetail)
        }
    }
    Column (
        modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)
    ) {
        TextFieldBox(filterText = filterText)
        LazyColumn {
            items(spellsFavoritesList.filter { spellDetail ->
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
                            spellsFavoritesList.remove(spellDetail)
                            saveFavoritesToPrefs(context, spellsFavoritesList)
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