package com.example.spellsdnd.navigation.favorites

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.spellsdnd.data.SpellDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Favorites {
    private val favoritesSpells = mutableStateListOf(
        mutableListOf<SpellDetail>(), // Индекс 0 для английского языка
        mutableListOf<SpellDetail>()  // Индекс 1 для русского языка
    )

    fun getFavoritesSpells(): SnapshotStateList<MutableList<SpellDetail>> {
        return favoritesSpells
    }

    fun getListBySelectedLanguage(selectedLanguage: MutableState<String>): MutableList<SpellDetail> {
        return when (selectedLanguage.value) {
            "ru" -> this.favoritesSpells[0]
            else -> this.favoritesSpells[1]
        }
    }

    fun setListBySelectedLanguage(selectedLanguage: MutableState<String>, otherList: MutableList<SpellDetail>) {
        when (selectedLanguage.value) {
            "ru" -> this.favoritesSpells[0] = otherList
            else -> this.favoritesSpells[1] = otherList
        }
    }

    private const val FAVORITES_PREFS_KEY = "favorites_prefs_key"
    // Функция сохранения списка избранных заклинаний в файл настроек
    fun saveFavoritesToPrefs(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            FAVORITES_PREFS_KEY, Context.MODE_PRIVATE
        )
        with(sharedPrefs.edit()) {
            val serializedFavorites = Gson().toJson(favoritesSpells)
            putString(FAVORITES_PREFS_KEY, serializedFavorites)
            apply()
        }
    }

    // Функция загрузки списка избранных заклинаний из файла настроек
    fun loadFavoritesFromPrefs(context: Context) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            FAVORITES_PREFS_KEY, Context.MODE_PRIVATE
        )
        val serializedFavorites = sharedPrefs.getString(FAVORITES_PREFS_KEY, null)
        if (serializedFavorites != null) {
            try {
                val type = object : TypeToken<SnapshotStateList<MutableList<SpellDetail>>>() {}.type
                val favorites: SnapshotStateList<MutableList<SpellDetail>> = Gson().fromJson(serializedFavorites, type)
                favoritesSpells.clear()
                favoritesSpells.addAll(favorites)
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}