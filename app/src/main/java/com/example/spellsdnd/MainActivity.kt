package com.example.spellsdnd

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spellsdnd.navigation.FavoritesScreen
import com.example.spellsdnd.navigation.FiltersScreen
import com.example.spellsdnd.navigation.HomeScreen
import com.example.spellsdnd.navigation.LoadingScreen
import com.example.spellsdnd.navigation.SettingsScreen
import com.example.spellsdnd.navigation.bar.NavItem
import com.example.spellsdnd.navigation.bar.Screens
import com.example.spellsdnd.objects.getSelectedLanguage
import com.example.spellsdnd.objects.getSharedPreferences
import com.example.spellsdnd.retrofit.SpellManager.getAllSpells
import com.example.spellsdnd.retrofit.SpellRusManager.getAllRusSpells
import com.example.spellsdnd.ui.theme.SpellsDnDTheme
import com.example.spellsdnd.utils.DarkBlueColorTheme
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

                //НИЖНЯЯ НАВИГАЦИОННАЯ ПАНЕЛЬ
                val navController = rememberNavController()
                val navItems = listOf(
                    NavItem(stringResource(id = R.string.filters), R.drawable.icon_filter, Screens.Filters),
                    NavItem(stringResource(id = R.string.home), R.drawable.icon_home_white_color, Screens.Home),
                    NavItem(stringResource(id = R.string.favorites), R.drawable.icon_favorites_white, Screens.Favorites),
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
                                    BottomNavigationItem(
                                        icon = {
                                            Image(
                                                painterResource(item.icon),
                                                contentDescription = null
                                            )
                                        },
                                        label = {
                                            Text(item.label,
                                                color = DarkBlueColorTheme.textColor,
                                                modifier = Modifier.align(alignment = Alignment.Bottom)
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
                            composable(Screens.Filters.route) {
                                FiltersScreen(onApplyFilter = { _ -> })
                            }
                            composable(Screens.Home.route) {
                                HomeScreen(selectedLanguage)
                            }
                            composable(Screens.Favorites.route) {
                                FavoritesScreen(selectedLanguage)
                            }
                            composable(Screens.Settings.route) {
                                SettingsScreen(selectedLanguage, sharedPreferences)
                            }
                        }
                    }
                }
            }
        }
        //spellListRequest(isLoading)
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
        getAllSpells { spells, error -> // Вызываем функцию getAllSpells и передаем ей список для обновления
            if (error != null) { // Обработка ошибки
                isLoading.value = false // Устанавливаем флаг isLoading в false при ошибке загрузки
                Log.e("not ok", "ERROR")
                return@getAllSpells
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