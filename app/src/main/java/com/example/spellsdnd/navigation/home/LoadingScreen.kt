package com.example.spellsdnd.navigation.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.utils.Utils

/**
 * Метод, который отрисовывает экран загрузки
 */
@Composable
fun LoadingScreen(isLoading: Boolean) {
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (isLoading) {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
            rotation.snapTo(0f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlueColorTheme.mainBackgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_wait),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .rotate(rotation.value),
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.please_wait),
            modifier = Modifier.padding(top = 30.dp)
                .wrapContentSize(Alignment.Center),
            style = MaterialTheme.typography.h6,
            color = DarkBlueColorTheme.textColor,
            fontFamily = Utils.customFont_im_fell_english_regular
        )
    }
}