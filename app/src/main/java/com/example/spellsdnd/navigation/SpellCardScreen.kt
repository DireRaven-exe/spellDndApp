package com.example.spellsdnd.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.InfoCardSide
import com.example.spellsdnd.navigation.MainCardSide
import com.example.spellsdnd.utils.DarkBlueColorTheme

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
fun SpellCardScreen(spellDetail: SpellDetail) {
    val cardState = remember { mutableStateOf(CardState.Front) }

    Column(Modifier
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
                MainCardSide(spellDetail = spellDetail, onClick = { cardState.value = CardState.Back })
            }
            CardState.Back -> {
                InfoCardSide(spellDetail = spellDetail, onClick = { cardState.value = CardState.Front })
            }
        }
    }
}

