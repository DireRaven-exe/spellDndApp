package com.example.spellsdnd.navigation.favorites

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.spellsdnd.data.SpellDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



object Homebrew {
    private const val HOMEBREW_PREFS_KEY = "homebrew_prefs_key"

    private val homebrewItems = mutableStateListOf(
        mutableListOf<SpellDetail>(), // Индекс 0 для заклинаний
        mutableListOf<SpellDetail>()  // Индекс 1 для другого компанента
    )

    fun getHomebrewPrefsKey(): String {
        return HOMEBREW_PREFS_KEY
    }

    fun getHomebrewItems() : SnapshotStateList<MutableList<SpellDetail>> {
        return homebrewItems
    }

    fun setHomebrewItems(otherList: MutableList<SpellDetail>) {
        homebrewItems[0] = otherList
    }
}

object Favorites {
    private const val FAVORITES_PREFS_KEY = "favorites_prefs_key"

    private val favoritesSpells = mutableStateListOf(
        mutableListOf<SpellDetail>(), // Индекс 0 для английского языка
        mutableListOf<SpellDetail>()  // Индекс 1 для русского языка
    )

    fun getFavoritesPrefsKet(): String {
        return FAVORITES_PREFS_KEY
    }

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




}

// Функция сохранения списка избранных заклинаний в файл настроек
fun saveFavoritesToPrefs(context: Context, savedSpells: SnapshotStateList<MutableList<SpellDetail>>, prefsKey: String) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        prefsKey, Context.MODE_PRIVATE
    )
    with(sharedPrefs.edit()) {
        val serializedFavorites = Gson().toJson(savedSpells)
        putString(prefsKey, serializedFavorites)
        apply()
    }
}

// Функция загрузки списка избранных заклинаний из файла настроек
fun loadFavoritesFromPrefs(context: Context, savedSpells: SnapshotStateList<MutableList<SpellDetail>>, prefsKey: String) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        prefsKey, Context.MODE_PRIVATE
    )
    val serializedFavorites = sharedPrefs.getString(prefsKey, null)
    if (serializedFavorites != null) {
        try {
            val type = object : TypeToken<SnapshotStateList<MutableList<SpellDetail>>>() {}.type
            val favorites: SnapshotStateList<MutableList<SpellDetail>> = Gson().fromJson(serializedFavorites, type)
            savedSpells.clear()
            savedSpells.addAll(favorites)
        } catch (e: Exception) {
            // Обработка ошибки
        }
    }
}