package com.example.spellsdnd.menuActions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.ui.theme.SpellDndTheme


/**
* Composable функция ShowMenuActions отображает выпадающее меню с действиями.
* @param showMenu MutableState<Boolean> состояние меню (отображается/скрыто)
* @param itemCount Int количество элементов в меню
* @param menuItems List<MenuActionsItem> список элементов меню с их заголовками и иконками
 */
@Composable
fun ShowMenuActions(
    showMenu: MutableState<Boolean>,
    itemCount: Int,
    menuItems: List<MenuActionsItem>
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(surface = SpellDndTheme.colors.secondaryBackground),
        shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(12))
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { showMenu.value = false },
            modifier = Modifier
                .width(300.dp)
        ) {
            for (index in 0 until itemCount) {
                val menuItem = menuItems.getOrNull(index)
                if (menuItem != null) {
                    DropdownMenuItem(
                        onClick = {
                            showMenu.value = false
                            menuItem.action()
                        },
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 0.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = menuItem.title,
                                modifier = Modifier.weight(1f),
                                color = SpellDndTheme.colors.primaryText
                            )
                            Icon(
                                painter = painterResource(menuItem.icon),
                                contentDescription = menuItem.title,
                                tint = SpellDndTheme.colors.primaryIcon,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                    if (index < itemCount - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .padding(start = 0.dp)
                        ) {
                            Divider(color = SpellDndTheme.colors.tintColor)
                        }
                    }

                }
            }
        }
    }
}