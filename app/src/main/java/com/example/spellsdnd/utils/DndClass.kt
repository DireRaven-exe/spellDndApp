package com.example.spellsdnd.utils
import androidx.compose.runtime.Composable
import com.example.spellsdnd.R

/**
 * Метод, который возвращает иконку класса
 * @param dndClass - название класса DnD
 */
@Composable
fun getIconResId(dndClass: DndClass): Int {
    return when (dndClass) {
            DndClass.BARBARIAN -> R.drawable.icon_barbarian
            DndClass.BARD -> R.drawable.icon_bard
            DndClass.CLERIC -> R.drawable.icon_cleric
            DndClass.DRUID -> R.drawable.icon_druid
            DndClass.FIGHTER -> R.drawable.icon_fighter
            DndClass.MONK -> R.drawable.icon_monk
            DndClass.PALADIN -> R.drawable.icon_paladin
            DndClass.RANGER -> R.drawable.icon_ranger
            DndClass.ROGUE -> R.drawable.icon_rogue
            DndClass.SORCERER -> R.drawable.icon_sorcerer
            DndClass.WARLOCK -> R.drawable.icon_warlock
            DndClass.WIZARD -> R.drawable.icon_wizard
            DndClass.EMPTY -> R.drawable.icon_all
            DndClass.ARTIFICER -> R.drawable.icon_artificer
    }
}

/**
 * Перечисление всех классов DnD
 */
enum class DndClass {
        BARBARIAN,
        BARD,
        CLERIC,
        DRUID,
        FIGHTER,
        MONK,
        PALADIN,
        RANGER,
        ROGUE,
        SORCERER,
        WARLOCK,
        WIZARD,
        ARTIFICER,
        EMPTY
}