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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R


/**
 * Метод, который отображает TextField для поиска заклинаний
 */
@Composable
fun TextFieldBox(filterText: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val textFieldValue = remember { mutableStateOf(TextFieldValue(filterText.value)) }
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Card {
            TextField(
                value = textFieldValue.value,
                onValueChange = { newValue ->
                    textFieldValue.value = newValue
                    filterText.value = newValue.text
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
                    backgroundColor = DarkBlueColorTheme.mainBackgroundColor,
                    textColor = DarkBlueColorTheme.textColor,
                    cursorColor = DarkBlueColorTheme.textFieldCursorColor,
                    focusedIndicatorColor = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                    unfocusedIndicatorColor = DarkBlueColorTheme.textFieldUnfocusedIndicatorColor
                ),
                label = {
                    Text(
                        text = stringResource(R.string.Search_spell),
                        color = DarkBlueColorTheme.textFieldTitleColor,
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
                    textFieldValue.value = TextFieldValue("")
                    keyboardController?.hide()
                    isKeyboardVisible.value = false
                    focusManager.clearFocus()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = DarkBlueColorTheme.textButtonColor,
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