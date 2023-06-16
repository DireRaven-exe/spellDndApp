package com.example.spellsdnd.navigation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsScreen() {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Favorites", "Homebrew")
    val pagerState = rememberPagerState() // 2.
    Column {
        TabRow(selectedTabIndex = tabIndex,
            indicator = { tabPositions -> // 3.
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(
                        pagerState,
                        tabPositions
                    )
                )
            }) {
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
                    // Content for Tab 1
                    Text("Content for Tab 1", color = Color.White)
                }
                1 -> {
                    // Content for Tab 2
                    Text("Content for Tab 2", color = Color.White)
                }
            }
        }
    }
}