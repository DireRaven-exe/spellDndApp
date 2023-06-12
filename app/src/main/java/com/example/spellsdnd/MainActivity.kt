package com.example.spellsdnd

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.favorites.Favorites
import com.example.spellsdnd.navigation.favorites.FavoritesScreen
import com.example.spellsdnd.navigation.filter.FiltersScreen
import com.example.spellsdnd.navigation.home.HomeScreen
import com.example.spellsdnd.navigation.home.LoadingScreen
import com.example.spellsdnd.navigation.navItem.bar.NavItem
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.settings.SettingsScreen
import com.example.spellsdnd.navigation.settings.getSelectedLanguage
import com.example.spellsdnd.navigation.settings.getSharedPreferences
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.retrofit.SpellRusManager.getAllEnSpells
import com.example.spellsdnd.retrofit.SpellRusManager.getAllRusSpells
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme.screenActiveColor
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme.screenInactiveColor
import com.example.spellsdnd.ui.theme.SpellsDnDTheme
import com.example.spellsdnd.utils.MutableListManager.originalSpellsList
import com.example.spellsdnd.utils.MutableListManager.spellsList
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val isLoading = mutableStateOf(true) // Флаг состояния загрузки данных
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpellsDnDTheme {
                //ЯЗЫК ПРИЛОЖЕНИЯ
                val context = LocalContext.current
                val sharedPreferences = remember { getSharedPreferences(context) }
                val selectedLanguage = remember { mutableStateOf(getSelectedLanguage(sharedPreferences)) }

                val locale = Locale(selectedLanguage.value)
                Locale.setDefault(locale)
                val configuration = Configuration()
                configuration.setLocale(locale)
                context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

                // Устанавливаем язык и выполняем запрос после его установки
                LaunchedEffect(selectedLanguage.value) {
                    spellListRequest(isLoading, selectedLanguage.value)
                }

                Favorites.loadFavoritesFromPrefs(context)

                //НИЖНЯЯ НАВИГАЦИОННАЯ ПАНЕЛЬ
                val navController = rememberNavController()
                val navItems = listOf(
                    NavItem(stringResource(id = R.string.filters), R.drawable.icon_filter, Screens.Filters),
                    NavItem(stringResource(id = R.string.home), R.drawable.icon_home, Screens.Home),
                    NavItem(stringResource(id = R.string.favorites), R.drawable.icon_favorites_not_added, Screens.Favorites),
                    NavItem(stringResource(id = R.string.settings), R.drawable.icon_settings, Screens.Settings),
                )
                var currentScreen by remember { mutableStateOf<Screens>(Screens.Home) }

                if (isLoading.value) { LoadingScreen(isLoading = isLoading.value) }
                else {
                    Scaffold(
                        backgroundColor = Color(0xFF151829),
                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = DarkBlueColorTheme.bottomBarBackgroundColor
                            ) {
                                navItems.forEach { item ->
                                    val selectedIconColor = if (currentScreen == item.route) {
                                        screenActiveColor // Цвет иконки, когда находимся на соответствующем экране
                                    } else {
                                        screenInactiveColor// Цвет иконки для остальных экранов
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
                        NavScreens(navController, innerPadding, selectedLanguage, sharedPreferences)
                    }
                }
            }
        }
    }

    @Composable
    private fun NavScreens(
        navController: NavHostController,
        innerPadding: PaddingValues,
        selectedLanguage: MutableState<String>,
        sharedPreferences: SharedPreferences
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Filters.route) {
                FiltersScreen(selectedLanguage, onApplyFilter = { _ -> })
            }
            composable(Screens.Home.route) {
                HomeScreen(navController, selectedLanguage)
            }
            composable(Screens.Favorites.route) {
                FavoritesScreen(navController, selectedLanguage)
            }
            composable(Screens.Settings.route) {
                SettingsScreen(selectedLanguage, sharedPreferences)
            }
            composable(
                route = Screens.Spell.route,
                arguments = listOf(navArgument("slug") { type = NavType.StringType })
            ) { backStackEntry ->
                val spellSlug = backStackEntry.arguments?.getString("slug")
                if (spellSlug != null) {
                    val spellDetail = getSpellDetailBySlug(spellSlug)
                    if (spellDetail != null) {
                        SpellCardScreen(
                            selectedLanguage = selectedLanguage,
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
                val spellDetail = getSpellDetailBySlug(spellSlug)
                if (spellDetail != null) {
                    SpellCardScreen(
                        selectedLanguage = selectedLanguage,
                        navController = navController,
                        spellDetail = spellDetail,
                        isPinnedAndIsFavoriteScreen = Pair(true, true)
                    )
                } else {
                    // Обработка случая, когда детали заклинания не найдены
                }
            }
        }
    }
}

//PinSpellCard(context, selectedLanguage, spellDetail!!, navController, isFavorite = true)
fun getSpellDetailBySlug(slug: String): SpellDetail? {
    return spellsList.find {
        Log.e("slug", it.slug)
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