package com.example.spellsdnd.navigation.spell.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.utils.Utils
import com.example.spellsdnd.utils.applySchoolStyle

/**
 * Метод, отображающий информацию об отдельном заклинании
 * @param spellDetail - информация о заклинании
 */
@Composable
fun InfoCardSide(spellDetail: SpellDetail, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(
                top = 0.dp,
                start = 10.dp,
                end = 10.dp,
                bottom = 30.dp
            )
            .aspectRatio(0.65f)
            .wrapContentHeight()
            .clickable { onClick.invoke() },
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(applySchoolStyle(spellDetail = spellDetail).second)
                .padding(16.dp)
        ) {
            Column {
                //NAME, SCHOOL AND LEVEL
                NameAndSchoolBox(spellDetail = spellDetail)
                //PARAMETERS
                ParametersBox(spellDetail = spellDetail)
                //DESCRIPTION
                DescriptionBox(spellDetail = spellDetail)
                //MATERIAL
                MaterialBox(spellDetail = spellDetail)
            }
        }
    }
}

@Composable
fun checkMaterial(material: String): String {
    return if (material == "") {
        stringResource(R.string.none_material)
    } else {
        material
    }
}

@Composable
fun NameAndSchoolBox(spellDetail: SpellDetail) {
    //NAME, SCHOOL AND LEVEL
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(
                text = spellDetail.name.uppercase(),
                style = MaterialTheme.typography.h6,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 1.dp)
                    .align(Alignment.CenterHorizontally),
                fontFamily = Utils.customFont_im_fell_english_regular,
                fontSize = MaterialTheme.typography.h6.fontSize * 0.8f
            )
            Text(
                text = spellDetail.level + ", " + spellDetail.school,
                style = MaterialTheme.typography.h6,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .align(Alignment.CenterHorizontally),
                //fontFamily = Utils.customFont_im_fell_english_regular,
                fontSize = MaterialTheme.typography.h6.fontSize * 0.7f
            )
        }

    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun ParametersBox(spellDetail: SpellDetail) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE1D1B2)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            Column(
                modifier = Modifier
            ) {
                ShowParameter(
                    iconParameter = R.drawable.icon_radius,
                    nameParameter = stringResource(R.string.range),
                    textParameter = spellDetail.range
                )
                ShowParameter(
                    iconParameter = R.drawable.icon_duration,
                    nameParameter = stringResource(R.string.duration),
                    textParameter = spellDetail.duration
                )
            }
            Column(
                modifier = Modifier
            ) {
                ShowParameter(
                    iconParameter = R.drawable.icon_casting_time,
                    nameParameter = stringResource(R.string.casting_time),
                    textParameter = spellDetail.casting_time
                )
                ShowParameter(
                    iconParameter = R.drawable.icon_components,
                    nameParameter = stringResource(R.string.components),
                    textParameter = spellDetail.components
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun ShowParameter(iconParameter: Int, nameParameter: String, textParameter: String) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp),

    ) {
        Icon(
            painter = painterResource(id = iconParameter),
            contentDescription = null,
            modifier = Modifier
                .scale(1f) // Увеличиваем размер изображения
                .padding(end = 8.dp)
        )
        Column() {
            Text(
                text = nameParameter.uppercase(),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = MaterialTheme.typography.h6.fontSize * 0.55f
                ), // Уменьшаем размер шрифта
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = textParameter.uppercase(),
                style = MaterialTheme.typography.h6.copy(
                    fontSize = MaterialTheme.typography.h6.fontSize * 0.5f
                ), // Уменьшаем размер шрифта
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
}

@Composable
fun DescriptionBox(spellDetail: SpellDetail) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE1D1B2))
    ) {
        LazyColumn(modifier = Modifier.height(180.dp).fillMaxWidth()) {
            item {
                Text(
                    text = checkDesc(spellDetail),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    //fontFamily = Utils.customFont_im_fell_english_regular,
                    fontSize = MaterialTheme.typography.h6.fontSize * 0.7f,
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                )
            }
        }
    }
}
@Composable
fun checkDesc(spellDetail: SpellDetail) : String {
    return if (spellDetail.higher_level == "") {
        spellDetail.desc
    }
    else spellDetail.desc + "\n\n" + stringResource(R.string.higher_level) + spellDetail.higher_level
}

@Composable
fun MaterialBox(spellDetail: SpellDetail) {
    Column {
        Text(
            text = stringResource(R.string.material),
            style = MaterialTheme.typography.body2,
            color = Color.White,
            fontFamily = Utils.customFont_im_fell_english_regular,
            fontSize = MaterialTheme.typography.h6.fontSize * 0.7f,
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 5.dp
                )
                .align(Alignment.CenterHorizontally),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE1D1B2))
        ) {
            LazyColumn {
                item {
                    Text(
                        text = checkMaterial(spellDetail.material),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        fontFamily = Utils.customFont_im_fell_english_regular,
                        fontSize = MaterialTheme.typography.h6.fontSize * 0.7f,
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                top = 5.dp,
                                end = 10.dp,
                                bottom = 5.dp
                            )
                    )
                }
            }
        }
    }
}