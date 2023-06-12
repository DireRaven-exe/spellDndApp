package com.example.spellsdnd.menuActions


/**
 * MenuActionsItem отвечает за один элемент из списка дейсвий в меню "выбор действия"
 */
data class MenuActionsItem(
    val title: String,
    val icon: Int,
    val action: () -> Unit
)
