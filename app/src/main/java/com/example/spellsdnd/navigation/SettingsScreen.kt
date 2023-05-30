package com.example.spellsdnd.navigation

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.spellsdnd.R
import com.example.spellsdnd.objects.saveLanguageToSharedPreferences
import com.example.spellsdnd.utils.DarkBlueColorTheme
import java.util.Locale

@Composable
fun SettingsScreen(
    selectedLanguage: MutableState<String>,
    sharedPreferences: SharedPreferences,
) {
//    val context = LocalContext.current
//    val sharedPreferences = remember { getSharedPreferences(context) }
//
//    val selectedLanguage = remember { mutableStateOf(getSelectedLanguage(sharedPreferences)) }



    //Surface(modifier = Modifier.background(DarkBlueColorTheme.mainBackgroundColor)) {
        Card(modifier = Modifier
            .padding(start = 5.dp, end = 0.dp)
            .background(DarkBlueColorTheme.mainBackgroundColor)) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = stringResource(R.string.language))

                RadioButton(
                    selected = selectedLanguage.value == "en",
                    onClick = {
                        selectedLanguage.value = "en"
                        saveLanguageToSharedPreferences(sharedPreferences, "en")
                    }
                )

                Text(
                    text = stringResource(R.string.english),
                    modifier = Modifier.padding(start = 24.dp)
                )

                RadioButton(
                    selected = selectedLanguage.value == "ru",
                    onClick = {
                        selectedLanguage.value = "ru"
                        saveLanguageToSharedPreferences(sharedPreferences, "ru")
                    }
                )

                Text(
                    text = stringResource(R.string.russian),
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        }
    //}
}


