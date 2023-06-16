@file:OptIn(ExperimentalPagerApi::class)

package com.example.spellsdnd.navigation.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.menuActions.MenuActionsItem
import com.example.spellsdnd.menuActions.ShowMenuActions
import com.example.spellsdnd.navigation.favorites.Favorites.getListBySelectedLanguage
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewItems
import com.example.spellsdnd.navigation.favorites.Homebrew.getHomebrewPrefsKey
import com.example.spellsdnd.navigation.favorites.Homebrew.setHomebrewItems
import com.example.spellsdnd.navigation.navItem.bar.Screens
import com.example.spellsdnd.navigation.settings.Settings
import com.example.spellsdnd.navigation.spell.CardState
import com.example.spellsdnd.navigation.spell.ShowCardSide
import com.example.spellsdnd.navigation.spell.SpellCardScreen
import com.example.spellsdnd.ui.theme.SpellDndTheme
import com.example.spellsdnd.utils.TextFieldBox
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * Метод, который отрисовывает поле для поиска/фильтра
 * избранных заклинаний и сами карточки заклинаний
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FavoritesScreen(navController: NavController, settingsApp: Settings, context: Context) {
    val filterText = remember { mutableStateOf("") }
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        stringResource(id = R.string.favorites),
        stringResource(id = R.string.homebrew)
    )
    val pagerState = rememberPagerState() // 2.
    Scaffold(backgroundColor = SpellDndTheme.colors.primaryBackground) {
        Column(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
            TextFieldBox(filterText = filterText)
            TabRow(
                selectedTabIndex = tabIndex,
                indicator = { tabPositions -> // 3.
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(
                            pagerState,
                            tabPositions
                        )
                    )
                },
                backgroundColor = SpellDndTheme.colors.primaryBackground,
                modifier = Modifier.height(40.dp),
                contentColor = SpellDndTheme.colors.primaryText,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = title) })
                }
            }
            HorizontalPager( // 4.
                count = tabTitles.size,
                state = pagerState,
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> {
                        FavoritesList(filterText, settingsApp, navController)
                    }
                    1 -> {
                        HomebrewScreen(navController, context)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesList(
    filterText: MutableState<String>,
    settingsApp: Settings,
    navController: NavController
) {
    LazyColumn(modifier = Modifier.background(SpellDndTheme.colors.primaryBackground)) {
        items(getListBySelectedLanguage(settingsApp.selectedLanguage).filter { spellDetail ->
            spellDetail.school.contains(filterText.value, ignoreCase = true) ||
                    spellDetail.name.contains(filterText.value, ignoreCase = true)
        }
        ) { spellDetail ->
            SpellCardScreen(
                settingsApp = settingsApp,
                navController = navController,
                spellDetail = spellDetail,
                isPinnedAndIsFavoriteScreen = Pair(false, true)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                modifier = Modifier.height(6.dp),
                color = SpellDndTheme.colors.secondaryBackground
            )
        }
    }
}

