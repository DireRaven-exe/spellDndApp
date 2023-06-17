package com.example.spellsdnd.navigation.favorites.homebrew

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.spellsdnd.data.SpellCreationState
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.navigation.filter.DataForFilter
import com.example.spellsdnd.navigation.filter.DataForFilter.getListClassesBySelectedLanguage
import com.example.spellsdnd.navigation.settings.CardWithDividers
import com.example.spellsdnd.navigation.settings.CreateSpacerWithDivider
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.ButtonNotSelected
import com.example.spellsdnd.utils.ButtonSelected
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateHomebrewItem(
    sheetState: ModalBottomSheetState,
    spellCreationState: SpellCreationState,
    settingsApp: Settings,
    onCreate: (SpellDetail) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.create_card),
                        color = SpellDndTheme.colors.primaryText,
                        fontWeight = SpellDndTheme.typography.heading.fontWeight,
                        fontSize = SpellDndTheme.typography.heading.fontSize
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp
            )
        },
        backgroundColor = SpellDndTheme.colors.primaryBackground
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                CardWithDividers {
                    CreateTextField(stringResource(id = R.string.spellName), spellCreationState.name)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(id = R.string.spellDesc), spellCreationState.desc)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(id = R.string.spellHightLevel), spellCreationState.higherLevel)
                    CreateSpacerWithDivider()

                    CreateRowScrollableButtons(
                        stringResource(id = R.string.level),
                        spellCreationState.level,
                        spellCreationState.levelInt,
                        listOf(stringResource(id = R.string.cantrip), "1", "2", "3", "4", "5", "6", "7", "8", "9")
                    )
                    CreateSpacerWithDivider()

                    CreateRowScrollableButtons(
                        stringResource(id = R.string.school),
                        spellCreationState.school,
                        spellCreationState.plug,
                        DataForFilter.getListSchoolsBySelectedLanguage(settingsApp.selectedLanguage)
                    )
                }
            }

            item {
                CardWithDividers {
                    CreateTextField(stringResource(id = R.string.range), spellCreationState.range)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(R.string.duration), spellCreationState.duration)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(id = R.string.casting_time), spellCreationState.castingTime)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(id = R.string.components), spellCreationState.components)
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(id = R.string.material), spellCreationState.material)
                }
            }

            item {
                CardWithDividers {
                    CreateRowScrollableButtons(
                        stringResource(R.string.needConcentration),
                        spellCreationState.concentration,
                        spellCreationState.plug,
                        listOf(stringResource(id = R.string.yes), stringResource(id = R.string.no))
                    )
                    CreateSpacerWithDivider()

                    CreateRowScrollableButtons(
                        stringResource(R.string.isRitual),
                        spellCreationState.ritual,
                        spellCreationState.plug,
                        listOf(stringResource(id = R.string.yes), stringResource(id = R.string.no))
                    )
                }
            }

            item {
                CardWithDividers {
                    CreateMultiSelectRowScrollableButtons(
                        label = stringResource(R.string.dnd_class),
                        selectedValues = spellCreationState.dndClass,
                        options = getListClassesBySelectedLanguage(settingsApp.selectedLanguage)
                    )
                    CreateSpacerWithDivider()

                    CreateTextField(stringResource(R.string.archetype), spellCreationState.archetype)
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            resetSpellCreationState(spellCreationState)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = SpellDndTheme.colors.buttonBackgroundColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = SpellDndTheme.colors.buttonContentColor
                        )
                    }

                    Button(
                        onClick = {
                            val spellDetail = SpellDetail(
                                slug = spellCreationState.slug.value,
                                name = spellCreationState.name.value,
                                desc = spellCreationState.desc.value,
                                higher_level = spellCreationState.higherLevel.value,
                                page = spellCreationState.page.value,
                                range = spellCreationState.range.value,
                                components = spellCreationState.components.value,
                                material = spellCreationState.material.value,
                                ritual = spellCreationState.ritual.value,
                                duration = spellCreationState.duration.value,
                                concentration = spellCreationState.concentration.value,
                                casting_time = spellCreationState.castingTime.value,
                                level = spellCreationState.level.value,
                                level_int = spellCreationState.levelInt.value,
                                school = spellCreationState.school.value,
                                dnd_class = spellCreationState.dndClass.value,
                                archetype = spellCreationState.archetype.value,
                                circles = "",
                                document__slug = "",
                                document__title = "",
                                document__license_url = ""
                            )
                            // Вызов функции обратного вызова с созданным экземпляром SpellDetail
                            onCreate(spellDetail)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = SpellDndTheme.colors.buttonBackgroundColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.accept),
                            color = SpellDndTheme.colors.buttonContentColor
                        )
                    }
                }
            }
        }
    }
}


/**
 * для очищения выделенных элементов
 */
private fun resetSpellCreationState(spellCreationState: SpellCreationState) {
    spellCreationState.slug.value = ""
    spellCreationState.name.value = ""
    spellCreationState.desc.value = ""
    spellCreationState.higherLevel.value = ""
    spellCreationState.page.value = ""
    spellCreationState.range.value = ""
    spellCreationState.components.value = ""
    spellCreationState.material.value = ""
    spellCreationState.ritual.value = ""
    spellCreationState.duration.value = ""
    spellCreationState.concentration.value = ""
    spellCreationState.castingTime.value = ""
    spellCreationState.level.value = ""
    spellCreationState.levelInt.value = 0
    spellCreationState.plug.value = 0
    spellCreationState.school.value = ""
    spellCreationState.dndClass.value = ""
    spellCreationState.archetype.value = ""
}


