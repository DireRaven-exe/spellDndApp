package com.example.spellsdnd.navigation.navItem.bar

const val SLUG = "slug"
sealed class Screens(val route: String) {
    object Filters : Screens("filters")
    object Home : Screens("home")
    object Favorites : Screens("favorites")
    object Settings : Screens("settings")
    object Spell : Screens(route = "home/{$SLUG}") {
        fun enterSlug(slug: String): String{
            return this.route.replace(oldValue = "{$SLUG}", newValue = slug)
        }
    }
    object FavoriteSpell : Screens("favorites/{$SLUG}") {
        fun enterSlug(slug: String): String{
            return this.route.replace(oldValue = "{$SLUG}", newValue = slug)
        }
    }
}
