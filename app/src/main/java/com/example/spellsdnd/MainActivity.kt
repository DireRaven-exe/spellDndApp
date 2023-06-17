package com.example.spellsdnd

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.favorites.Favorites.getFavoritesPrefsKet
import com.example.spellsdnd.navigation.favorites.Favorites.getFavoritesSpells
import com.example.spellsdnd.navigation.favorites.FavoritesScreen
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewItems
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewPrefsKey
import com.example.spellsdnd.navigation.favorites.loadFavoritesFromPrefs
import com.example.spellsdnd.navigation.home.HomeScreen
import com.example.spellsdnd.navigation.home.LoadingScreen
import com.example.spellsdnd.navigation.navItem.bar.NavItem
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.settings.SettingsScreen
import com.example.spellsdnd.navigation.settings.getSelectedLanguage
import com.example.spellsdnd.navigation.settings.getSelectedTheme
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.retrofit.SpellRusManager.getAllEnSpells
import com.example.spellsdnd.retrofit.SpellRusManager.getAllRusSpells
import com.example.spellsdnd.ui.theme.SpellDndSize
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList
import java.util.Locale


class MainActivity : ComponentActivity() {
    private val isLoading = mutableStateOf(true) // Флаг состояния загрузки данных
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingsApp: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            settingsApp = createSettingsFromSharedPreferences()

            SpellDndTheme(
                style = settingsApp.selectedStyle.value,
                textSize = settingsApp.selectedFontSize.value,
            ) {

                val locale = Locale(settingsApp.selectedLanguage.value)
                Locale.setDefault(locale)
                val configuration = Configuration()
                configuration.setLocale(locale)
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

                // Устанавливаем язык и выполняем запрос после его установки
                LaunchedEffect(settingsApp.selectedLanguage.value) {
                    spellListRequest(isLoading, settingsApp.selectedLanguage.value)
                }

                loadFavoritesFromPrefs(context, getFavoritesSpells(), getFavoritesPrefsKet())
                loadFavoritesFromPrefs(context, getHomebrewItems(), getHomebrewPrefsKey())

                //НИЖНЯЯ НАВИГАЦИОННАЯ ПАНЕЛЬ
                val navController = rememberNavController()
                val navItems = listOf(
                    NavItem(stringResource(id = R.string.favorites), R.drawable.icon_favorites_not_added, Screens.Favorites),
                    NavItem(stringResource(id = R.string.home), R.drawable.icon_home, Screens.Home),
                    NavItem(stringResource(id = R.string.settings), R.drawable.icon_settings, Screens.Settings),
                )
                var currentScreen by remember { mutableStateOf<Screens>(Screens.Home) }

                if (isLoading.value) { LoadingScreen(isLoading = isLoading.value) }
                else {
                    Scaffold(
                        backgroundColor = SpellDndTheme.colors.primaryBackground,
                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = SpellDndTheme.colors.secondaryBackground,
                                modifier = Modifier.height(50.dp),
                            ) {
                                navItems.forEach { item ->
                                    val selectedIconColor = if (currentScreen == item.route) {
                                        SpellDndTheme.colors.primaryText
                                    } else {
                                        SpellDndTheme.colors.secondaryText
                                    }
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                painterResource(item.icon),
                                                contentDescription = null,
                                                tint = selectedIconColor
                                            )
                                        },
                                        label = {
                                            Text(item.label,
                                                color = selectedIconColor,
                                            ) },
                                        selected = currentScreen == item.route,
                                        onClick = {
                                            currentScreen = item.route
                                            navController.navigate(item.route.route)
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screens.Home.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screens.Home.route) {
                                HomeScreen(navController, settingsApp)
                            }
                            composable(Screens.Favorites.route) {
                                FavoritesScreen(navController, settingsApp, context)
                            }
                            composable(Screens.Settings.route) {
                                SettingsScreen(settingsApp, sharedPreferences)
                            }
                            composable(
                                route = Screens.Spell.route,
                                arguments = listOf(navArgument("slug") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val spellSlug = backStackEntry.arguments?.getString("slug")
                                if (spellSlug != null) {
                                    val spellDetail = getSpellDetailBySlug(spellsList, spellSlug)
                                    if (spellDetail != null) {
                                        SpellCardScreen(
                                            settingsApp = settingsApp,
                                            navController = navController,
                                            spellDetail = spellDetail,
                                            isPinnedAndIsFavoriteScreen = Pair(true, false)
                                        )
                                    } else {
                                        // Обработка случая, когда детали заклинания не найдены
                                    }
                                } else {
                                    // Обработка случая, когда spellSlug является нулевым
                                }
                            }
                            composable(
                                route = Screens.FavoriteSpell.route,
                                arguments = listOf(navArgument("slug") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val spellSlug = backStackEntry.arguments?.getString("slug").toString()
                                val spellDetail = getSpellDetailBySlug(spellsList, spellSlug)
                                Log.e("slag", spellSlug)
                                if (spellDetail != null) {
                                    SpellCardScreen(
                                        settingsApp = settingsApp,
                                        navController = navController,
                                        spellDetail = spellDetail,
                                        isPinnedAndIsFavoriteScreen = Pair(true, true)
                                    )
                                } else {
                                    val newSpellDetail = getNewSpellDetailBySlug(spellSlug) // Получение информации о новом заклинании
                                    if (newSpellDetail != null) {
                                        SpellCardScreen(
                                            settingsApp = settingsApp,
                                            navController = navController,
                                            spellDetail = newSpellDetail,
                                            isPinnedAndIsFavoriteScreen = Pair(true, true)
                                        )
                                    } else {
                                        // Обработка случая, когда детали нового заклинания не найдены
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveSettingsToSharedPreferences()
    }

    private fun createSettingsFromSharedPreferences(): Settings {
        val selectedLanguage = mutableStateOf(getSelectedLanguage(sharedPreferences))
        val selectedStyle = mutableStateOf(getSelectedTheme(sharedPreferences))
        val selectedFontSize = mutableStateOf(SpellDndSize.Medium)

        return Settings(
            selectedLanguage = selectedLanguage,
            selectedStyle = selectedStyle,
            selectedFontSize = selectedFontSize
        )
    }

    private fun saveSettingsToSharedPreferences() {
        with(sharedPreferences.edit()) {
            putString("selectedLanguage", settingsApp.selectedLanguage.value)
            putString("selectedStyle", settingsApp.selectedStyle.value.toString())
            putString("selectedFontSize", settingsApp.selectedFontSize.value.name)
            apply()
        }
    }
}
fun getSpellDetailBySlug(spellsList: SnapshotStateList<SpellDetail>, slug: String): SpellDetail? {
    return spellsList.find {
        it.slug == slug
    }
}

fun getNewSpellDetailBySlug(slug: String): SpellDetail? {
    return getHomebrewItems()[0].find {
        it.slug == slug
    }
}

/**
 * Метод, который выполняет запрос для заполнения списка заклинаний
 */
fun spellListRequest(isLoading: MutableState<Boolean>, value: String) {
    spellsList.clear()
    originalSpellsList.clear()
    if (value == "ru") {
        getAllRusSpells { spells, error -> // Вызываем функцию getAllRusSpells и передаем ей список для обновления
            if (error != null) { // Обработка ошибки
                isLoading.value = false // Устанавливаем флаг isLoading в false при ошибке загрузки
                Log.e("not ok", "ERROR")
                return@getAllRusSpells
            }
            if (spells != null) {
                spellsList.addAll(spells) // Добавляем полученные заклинания в список spellsList
                originalSpellsList.addAll(spellsList)

                isLoading.value =
                    false // Устанавливаем флаг isLoading в false по завершении загрузки
            }
        }
    }
    else {
        getAllEnSpells { spells, error -> // Вызываем функцию getAllSpells и передаем ей список для обновления
            if (error != null) { // Обработка ошибки
                isLoading.value = false // Устанавливаем флаг isLoading в false при ошибке загрузки
                Log.e("not ok", "ERROR")
                return@getAllEnSpells
            }
            if (spells != null) {
                spellsList.addAll(spells) // Добавляем полученные заклинания в список spellsList
                originalSpellsList.addAll(spellsList)
                isLoading.value =
                    false // Устанавливаем флаг isLoading в false по завершении загрузки
            }
        }
    }
}