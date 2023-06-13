package com.example.spellsdnd.navigation.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.ui.theme.SpellDndStyle

/**
 *  getSharedPreferences - Получение объекта SharedPreferences
 *  Создает и возвращает объект SharedPreferences с именем "settings" и режимом Context.MODE_PRIVATE
 *  @param context - контекст приложения
 *  @return SharedPreferences
 */
fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
}

/**
 * getSelectedLanguage - Получение выбранного языка из SharedPreferences
 * Возвращает выбранный язык из объекта SharedPreferences
 * @param sharedPreferences - объект SharedPreferences
 * @return выбранный язык (строка)
 */
fun getSelectedLanguage(sharedPreferences: SharedPreferences): String {
    return sharedPreferences.getString("language", "en") ?: "ru"
}

fun getSelectedThemeToString(sharedPreferences: SharedPreferences): String {
    return sharedPreferences.getString("theme", SpellDndStyle.DarkBlue.toString()) ?: SpellDndStyle.Dark.toString()
}

fun getSelectedTheme(sharedPreferences: SharedPreferences) : SpellDndStyle {
    return when (getSelectedThemeToString(sharedPreferences)) {
        "DarkBlue" -> SpellDndStyle.DarkBlue
        "Dark" -> SpellDndStyle.Dark
        else -> SpellDndStyle.Light
    }
}


/**
 * saveLanguageToSharedPreferences - Сохранение языка в SharedPreferences
 * Сохраняет выбранный язык в объекте SharedPreferences
 * @param sharedPreferences - объект SharedPreferences
 * @reurn language - выбранный язык (строка)
 */
fun saveLanguageToSharedPreferences(sharedPreferences: SharedPreferences, language: String) {
    sharedPreferences.edit().putString("language", language).apply()
}

fun saveThemeToSharedPreferences(sharedPreferences: SharedPreferences, theme: SpellDndStyle) {
    sharedPreferences.edit().putString("theme", theme.toString()).apply()
}

