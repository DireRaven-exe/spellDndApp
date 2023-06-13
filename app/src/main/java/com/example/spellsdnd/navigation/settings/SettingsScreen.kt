package com.example.spellsdnd.navigation.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.ui.theme.SpellDndStyle
import com.example.spellsdnd.ui.theme.SpellDndTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(settingsApp: Settings, sharedPreferences: SharedPreferences) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(id = R.string.settings),
                    color = SpellDndTheme.colors.primaryText,
                    fontWeight = SpellDndTheme.typography.heading.fontWeight,
                ) },
                backgroundColor = SpellDndTheme.colors.secondaryBackground,
                modifier = Modifier.fillMaxWidth()
            )
        },
        backgroundColor = SpellDndTheme.colors.primaryBackground
    ) {
        Card(
            modifier = Modifier
                .background(SpellDndTheme.colors.primaryBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(SpellDndTheme.colors.secondaryBackground)
            ) {
                item {
                    LanguageSelectionBox(
                        settingsApp = settingsApp,
                        sharedPreferences = sharedPreferences
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(start = 48.dp)
                    ) {
                        Divider(color = SpellDndTheme.colors.tintColor)
                    }
                }
                item {
                    ThemeSelectionBox(
                        selectedTheme = settingsApp.selectedStyle,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionBox(
    settingsApp: Settings,
    sharedPreferences: SharedPreferences,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.clickable { expanded = true }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(SpellDndTheme.colors.secondaryBackground)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_settings_language),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(32.dp),
                tint = SpellDndTheme.colors.primaryIcon
            )

            Text(
                text = stringResource(R.string.language),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                color = SpellDndTheme.colors.primaryText,
                fontSize = SpellDndTheme.typography.body.fontSize,
                fontWeight = SpellDndTheme.typography.body.fontWeight
            )
            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = when (settingsApp.selectedLanguage.value) {
                        "en" -> stringResource(R.string.english)
                        "ru" -> stringResource(R.string.russian)
                        else -> ""
                    },
                    color = SpellDndTheme.colors.primaryButtonTextColor,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
        }
    }

    if (expanded) {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(surface = SpellDndTheme.colors.secondaryBackground),
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(12))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(300.dp)
            ) {
                DropdownMenuItem(
                    onClick = {
                        settingsApp.selectedLanguage.value = "en"
                        saveLanguageToSharedPreferences(sharedPreferences, "en")
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.english),
                        color = SpellDndTheme.colors.primaryText
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(start = 0.dp)
                ) {
                    Divider(color = SpellDndTheme.colors.tintColor)
                }

                DropdownMenuItem(
                    onClick = {
                        settingsApp.selectedLanguage.value = "ru"
                        saveLanguageToSharedPreferences(sharedPreferences, "ru")
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.russian),
                        color = SpellDndTheme.colors.primaryText
                    )
                }
            }
        }
    }
}
@Composable
fun ThemeSelectionBox(
    selectedTheme: MutableState<SpellDndStyle>,
    sharedPreferences: SharedPreferences,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.clickable { expanded = true }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(SpellDndTheme.colors.secondaryBackground)
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_settings_theme),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(32.dp),
                tint = SpellDndTheme.colors.primaryIcon
            )

            Text(
                text = stringResource(R.string.theme),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                color = SpellDndTheme.colors.primaryText,
                fontSize = SpellDndTheme.typography.body.fontSize,
                fontWeight = SpellDndTheme.typography.body.fontWeight
            )
            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = when (selectedTheme.value) {
                        SpellDndStyle.DarkBlue -> stringResource(R.string.dark_blue)
                        SpellDndStyle.Light -> stringResource(R.string.light)
                        SpellDndStyle.Dark -> stringResource(id = R.string.dark)
                    },
                    color = SpellDndTheme.colors.primaryButtonTextColor,
                    modifier = Modifier.padding(end = 5.dp)
                )
            }
        }
    }
    if (expanded) {
        MaterialTheme(
            colors = MaterialTheme.colors.copy(surface = SpellDndTheme.colors.secondaryBackground),
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(12))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(300.dp)
            ) {
                DropdownMenuItem(
                    onClick = {
                        selectedTheme.value = SpellDndStyle.DarkBlue
                        saveThemeToSharedPreferences(sharedPreferences, selectedTheme.value)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.dark_blue),
                        color = SpellDndTheme.colors.primaryText
                    )
                }

                Divider(color = SpellDndTheme.colors.tintColor)

                DropdownMenuItem(
                    onClick = {
                        selectedTheme.value = SpellDndStyle.Dark
                        saveThemeToSharedPreferences(sharedPreferences, selectedTheme.value)
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.dark),
                        color = SpellDndTheme.colors.primaryText
                    )
                }

                Divider(color = SpellDndTheme.colors.tintColor)

                DropdownMenuItem(
                    onClick = {
                        selectedTheme.value = SpellDndStyle.Light
                        saveThemeToSharedPreferences(sharedPreferences, selectedTheme.value)
                        expanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.light),
                        color = SpellDndTheme.colors.primaryText
                    )
                }
            }
        }
    }
}





