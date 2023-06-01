package com.example.spellsdnd.navigation.settings

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.spellsdnd.R
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme.bottomBarBackgroundColor
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme.dropdownMenuBackgroundColor
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme.mainBackgroundColor

@Composable
fun SettingsScreen(
    selectedLanguage: MutableState<String>,
    sharedPreferences: SharedPreferences,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth().padding(top = 10.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                LanguageSelectionCard(
                    selectedLanguage = selectedLanguage,
                    sharedPreferences = sharedPreferences,
                )
            }
        }
    }
}

@Composable
fun LanguageSelectionCard(
    selectedLanguage: MutableState<String>,
    sharedPreferences: SharedPreferences,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.clickable { expanded = true }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(bottomBarBackgroundColor)
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = DarkBlueColorTheme.screenInactiveColor,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(48.dp.toPx(), size.height),
                        end = Offset(size.width, size.height)
                    )
                }
        ) {
            Image(
                painter = painterResource(R.drawable.icon_settings_language),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(32.dp)
            )

            Text(
                text = stringResource(R.string.language),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                color = DarkBlueColorTheme.textColor
            )
            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = when (selectedLanguage.value) {
                        "en" -> stringResource(R.string.english)
                        "ru" -> stringResource(R.string.russian)
                        else -> ""
                    },
                    color = DarkBlueColorTheme.screenActiveColor,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
        }
    }

    if (expanded) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = mainBackgroundColor).shadow(12.dp)
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth().fillMaxHeight()
                        .background(color = bottomBarBackgroundColor,
                            shape = RoundedCornerShape(16.dp)
                        ),
                ) {

                    DropdownMenuItem(
                        onClick = {
                            selectedLanguage.value = "en"
                            saveLanguageToSharedPreferences(sharedPreferences, "en")
                            expanded = false
                        },
                        //РИСУЕМ ЛИНИЮ ПОСЛЕ ЭЛЕМЕНТА
                        modifier = Modifier
                            //.fillMaxWidth()
                            .drawWithContent {
                                drawContent()
                                drawLine(
                                    color = DarkBlueColorTheme.screenInactiveColor,
                                    strokeWidth = 1.dp.toPx(),
                                    start = Offset(48.dp.toPx(), size.height),
                                    end = Offset(size.width - 48.dp.toPx(), size.height)
                                )
                            },
                    ) {
                        Text(
                            text = stringResource(R.string.english),
                            color = DarkBlueColorTheme.textColor
                        )
                    }
                    DropdownMenuItem(
                        onClick = {
                            selectedLanguage.value = "ru"
                            saveLanguageToSharedPreferences(sharedPreferences, "ru")
                            expanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(R.string.russian),
                            color = DarkBlueColorTheme.textColor
                        )
                    }
                }
        }
    }
}


