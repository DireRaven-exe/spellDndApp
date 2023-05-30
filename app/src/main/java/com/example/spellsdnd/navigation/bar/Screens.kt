package com.example.spellsdnd.navigation.bar

sealed class Screens(val route: String) {
    object Filters : Screens("filters")
    object Home : Screens("home")
    object Favorites : Screens("favorites")
    object Settings : Screens("settings")
}

