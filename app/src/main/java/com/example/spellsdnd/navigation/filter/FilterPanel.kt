package com.example.spellsdnd.navigation.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.ui.theme.DarkBlueColorTheme


/**
 * Метод, который отображает одно поле для фильтрации по выбранному параметру
 * (Spells, School, Level etc.)
 *
 * @param label: Это текстовое значение, которое будет
 * использоваться в качестве метки для поля ввода.
 *
 * @param items: Это список элементов, из которых пользователь может выбрать.
 *
 * @param selectedItems: Это множество выбранных элементов.
 *
 * @param onFilterChange: Это функция обратного вызова, которая будет
 * вызываться при изменении выбранных элементов.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterPanel(
    label: String,
    items: List<String>,
    selectedItems: MutableSet<String>,
    onFilterChange: (List<String>) -> Unit,
) {
    var textState by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val isKeyboardVisible = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Card(
        modifier = Modifier
            .background(DarkBlueColorTheme.mainBackgroundColor)
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(6.dp))
            .drawWithContent {
                drawContent()
                drawLine(
                    color = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height)
                )
            },
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier
                .height(53.dp)
                .background(DarkBlueColorTheme.mainBackgroundColor)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .background(DarkBlueColorTheme.mainBackgroundColor)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_list_filter),
                    contentDescription = "Expand $label menu",
                    modifier = Modifier.size(36.dp),
                )
            }
            TextField(
                value = textState,
                onValueChange = {
                    textState = it
                    selectedItems.clear()
                    selectedItems.addAll(
                        textState.split(",").map { it -> it.trim() })
                    onFilterChange(selectedItems.toList())
                },
                label = { Text(label, color = DarkBlueColorTheme.textFieldTitleColor) },
                modifier = Modifier
                    .weight(1f)
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
                    //Кнопка отмены
                    onClick = {
                        selectedItems.clear()
                        textState = "" // Очистка текстового поля
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
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(DarkBlueColorTheme.mainBackgroundColor)
                .border(
                    2.dp,
                    color = DarkBlueColorTheme.textFieldFocusedIndicatorColor,
                    shape = RoundedCornerShape(0.dp)
                )
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        if (selectedItems.contains(item)) {
                            selectedItems.remove(item)
                        } else {
                            selectedItems.add(item)
                        }
                        textState = selectedItems.sorted().joinToString(", ")
                        onFilterChange(selectedItems.toList())
                    }
                ) {
                    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
                        Checkbox(
                            checked = item in selectedItems,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = DarkBlueColorTheme.textFieldFocusedIndicatorColor
                            )
                        )
                        Text(
                            text = item,
                            modifier = Modifier.padding(start = 10.dp),
                            color = DarkBlueColorTheme.textColor
                        )
                    }
                }
            }
        }
    }
}