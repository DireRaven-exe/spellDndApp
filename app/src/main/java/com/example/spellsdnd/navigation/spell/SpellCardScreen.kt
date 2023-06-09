package com.example.spellsdnd.navigation.spell

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.spell.card.InfoCardSide
import com.example.spellsdnd.navigation.spell.card.MainCardSide
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.utils.Utils.isVisibleSpell

/**
 * Перечисление, обозначающее сторону карты
 */
enum class CardState {
    Front, Back
}

/**
 * Метод, который отвечает за навигацию
 * отображает либо основной экран (MainScreen)
 * либо экран с информацией о заклинании (InfoScreen)
 * @param spellDetail - информация о заклинании
 */
@Composable
fun SpellCardScreen(
    spellDetail: SpellDetail
) {
    val cardState = remember { mutableStateOf(CardState.Front) }
    Column(
        modifier = Modifier
            .background(DarkBlueColorTheme.mainBackgroundColor)
            .padding(
                top = 0.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        when (cardState.value) {
            CardState.Front -> {
                MainCardSide(
                    spellDetail = spellDetail,
                    onClick = { cardState.value = CardState.Back },
                )
            }
            CardState.Back -> {
                InfoCardSide(
                    spellDetail = spellDetail,
                    onClick = { cardState.value = CardState.Front },
                )
            }
        }
    }
}

