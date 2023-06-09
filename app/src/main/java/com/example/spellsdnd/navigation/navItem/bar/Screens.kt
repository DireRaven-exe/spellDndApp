package com.example.spellsdnd.navigation.navItem.bar

sealed class Screens(val route: String) {
    object Filters : Screens("filters")
    object Home : Screens("home")
    object Favorites : Screens("favorites")
    object Settings : Screens("settings")
    data class Spell(val slug: String) : Screens("spellDetail/$slug")
    data class FavoriteSpell(val slug: String, val isFavorite: Boolean) : Screens("favorites/spellDetail/$slug")
}

