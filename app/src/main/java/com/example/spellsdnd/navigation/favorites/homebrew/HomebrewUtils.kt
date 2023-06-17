package com.example.spellsdnd.navigation.favorites.homebrew

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.ButtonNotSelected
import com.example.spellsdnd.utils.ButtonSelected

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateTextField(
    label: String,
    valueState: MutableState<String>
) {
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    Column(modifier = Modifier
        .background(SpellDndTheme.colors.secondaryBackground)
        .fillMaxWidth()
    ) {
        TextField(
            value = valueState.value,
            onValueChange = { valueState.value = it },
            label = { Text(label, color = SpellDndTheme.colors.secondaryText) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                    isKeyboardVisible.value = true
                }
                .onFocusChanged { it -> isKeyboardVisible.value = it.isFocused },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = SpellDndTheme.colors.secondaryBackground,
                textColor = SpellDndTheme.colors.primaryText,
                cursorColor = SpellDndTheme.colors.tintColor,
                focusedIndicatorColor = SpellDndTheme.colors.secondaryBackground,
                unfocusedIndicatorColor = SpellDndTheme.colors.secondaryBackground
            ),
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
        if (isKeyboardVisible.value) {
            TextButton(
                modifier = Modifier,
                //Кнопка отмены
                onClick = {
                    valueState.value = "" // Очистка текстового поля
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
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    }
}

@Composable
fun CreateRowScrollableButtons(
    label: String,
    valueState: MutableState<String>,
    intValueState: MutableState<Int>,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(SpellDndTheme.colors.secondaryBackground)
                    .clickable { expanded = !expanded }
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    color = SpellDndTheme.colors.secondaryText,
                    fontSize = SpellDndTheme.typography.body.fontSize,
                    fontWeight = SpellDndTheme.typography.body.fontWeight
                )
                Text(
                    text = valueState.value,
                    color = SpellDndTheme.colors.primaryButtonTextColor,
                    fontSize = SpellDndTheme.typography.body.fontSize,
                    fontWeight = SpellDndTheme.typography.body.fontWeight
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    options.forEachIndexed { index, option ->
                        val isSelected = valueState.value.contains(option)
                        val buttonModifier = Modifier
                        val buttonTextModifier = Modifier.align(Alignment.CenterVertically)

                        if (isSelected) {
                            ButtonSelected(
                                onClick = {
                                    val selectedString = valueState.value
                                    if (selectedString.contains(option)) {
                                        val updatedString = selectedString.replace(option, "")
                                        valueState.value = updatedString.removeSuffix(option)
                                    } else {
                                        val updatedString = updateSelectedString(selectedString, option)
                                        valueState.value = updatedString
                                    }
                                },
                                modifier = buttonModifier,
                                textModifier = buttonTextModifier,
                                buttonText = option
                            )
                        } else {
                            ButtonNotSelected(
                                onClick = {
                                    valueState.value = option
                                    intValueState.value = index
                                },
                                modifier = buttonModifier,
                                textModifier = buttonTextModifier,
                                buttonText = option
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun CreateMultiSelectRowScrollableButtons(
    label: String,
    selectedValues: MutableState<String>,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(SpellDndTheme.colors.secondaryBackground)
                    .clickable { expanded = !expanded }
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    color = SpellDndTheme.colors.secondaryText,
                    fontSize = SpellDndTheme.typography.body.fontSize,
                    fontWeight = SpellDndTheme.typography.body.fontWeight
                )
                Text(
                    text = selectedValues.value,
                    color = SpellDndTheme.colors.primaryButtonTextColor,
                    fontSize = SpellDndTheme.typography.body.fontSize,
                    fontWeight = SpellDndTheme.typography.body.fontWeight
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    options.forEachIndexed { index, option ->
                        val isSelected = selectedValues.value.contains(option)
                        val buttonModifier = Modifier
                        val buttonTextModifier = Modifier.align(Alignment.CenterVertically)

                        if (isSelected) {
                            ButtonSelected(
                                onClick = {
                                    val selectedString = selectedValues.value
                                    if (selectedString.contains(option)) {
                                        val updatedString = selectedString.replace("$option, ", "")
                                        selectedValues.value = updatedString.removeSuffix(option)
                                    } else {
                                        val updatedString = updateSelectedString(selectedString, option)
                                        selectedValues.value = updatedString
                                    }
                                },
                                modifier = buttonModifier,
                                textModifier = buttonTextModifier,
                                buttonText = option
                            )
                        } else {
                            ButtonNotSelected(
                                onClick = {
                                    val selectedString = selectedValues.value
                                    if (selectedString.contains(option)) {
                                        val updatedString = selectedString.replace("$option, ", "")
                                        selectedValues.value = updatedString.removeSuffix(option)
                                    } else {
                                        val updatedString = updateSelectedString(selectedString, option)
                                        selectedValues.value = updatedString
                                    }
                                },
                                modifier = buttonModifier,
                                textModifier = buttonTextModifier,
                                buttonText = option
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun updateSelectedString(selectedString: String, option: String): String {
    val updatedString = when {
        selectedString.endsWith(", ") -> selectedString + option
        selectedString.isEmpty() -> option
        else -> "$selectedString, $option"
    }
    return updatedString
}