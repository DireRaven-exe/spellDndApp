@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.spellsdnd.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme
import com.example.spellsdnd.ui.theme.SpellDndTheme


/**
 * Метод, который отображает TextField для поиска заклинаний
 */
@Composable
fun TextFieldBox(filterText: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val textFieldValue = remember { mutableStateOf("") }
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Card(elevation = 0.dp) {
            TextField(
                value = textFieldValue.value,
                onValueChange = { newValue ->
                    textFieldValue.value = newValue
                    filterText.value = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                        isKeyboardVisible.value = true
                    }
                    .onFocusChanged { it ->
                        isKeyboardVisible.value = it.isFocused
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = SpellDndTheme.colors.primaryBackground,
                    textColor = SpellDndTheme.colors.primaryText,
                    cursorColor = SpellDndTheme.colors.tintColor,
                    focusedIndicatorColor = SpellDndTheme.colors.secondaryBackground,
                    unfocusedIndicatorColor = SpellDndTheme.colors.secondaryBackground
                ),
                label = {
                    Text(
                        text = stringResource(R.string.Search_spell),
                        color = SpellDndTheme.colors.secondaryText,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        isKeyboardVisible.value = false
                        focusManager.clearFocus()
                    }
                )
            )
        }

        if (isKeyboardVisible.value) {
            TextButton( //Кнопка отмены
                onClick = {
                    filterText.value = ""
                    textFieldValue.value = ""
                    keyboardController?.hide()
                    isKeyboardVisible.value = false
                    focusManager.clearFocus()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = SpellDndTheme.colors.primaryButtonTextColor,
                    disabledContentColor = Color.Gray,
                    disabledBackgroundColor = Color.LightGray,
                ),
                modifier = Modifier
                    .align(Alignment.CenterEnd),

            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    }
}