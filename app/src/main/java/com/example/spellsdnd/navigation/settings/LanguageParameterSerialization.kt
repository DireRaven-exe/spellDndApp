package com.example.spellsdnd.navigation.settings

import android.content.Context
import android.content.SharedPreferences

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

/**
 * saveLanguageToSharedPreferences - Сохранение языка в SharedPreferences
 * Сохраняет выбранный язык в объекте SharedPreferences
 * @param sharedPreferences - объект SharedPreferences
 * @reurn language - выбранный язык (строка)
 */
fun saveLanguageToSharedPreferences(sharedPreferences: SharedPreferences, language: String) {
    sharedPreferences.edit().putString("language", language).apply()
}
