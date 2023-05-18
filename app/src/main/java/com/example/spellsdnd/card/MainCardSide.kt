package com.example.spellsdnd.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spellsdnd.R
import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.utils.DndClass
import com.example.spellsdnd.utils.Utils
import com.example.spellsdnd.utils.checkDuration
import com.example.spellsdnd.utils.getIconResId
import com.example.spellsdnd.utils.isLongText
import com.example.spellsdnd.utils.schoolCheck
import java.util.Locale

/**
 * Метод, отображающий основной экран со списком карточек заклинаний
 * @param spellDetail - информация о заклинании
 */
@Composable
fun MainCardSide(spellDetail: SpellDetail, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 0.dp,
                start = 30.dp,
                end = 30.dp,
                bottom = 30.dp
            )
            .aspectRatio(0.65f)
            .wrapContentHeight()
            .clickable { onClick.invoke() },
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
    ) {
        DrawCardComponents(spellDetail = spellDetail)
    }
}

/**
 * Метод, который отрисовывает ВСЕ компоненты MainScreen
 * @param spellDetail - информация о заклинании
 */
@Composable
fun DrawCardComponents(spellDetail: SpellDetail) {
    Column(
        modifier = Modifier
            .background(schoolCheck(spellDetail).second) // Устанавливаем прозрачный фон
            .fillMaxSize()
    ) {
        TitleBox(spellDetail = spellDetail)
        ImageBox(spellDetail = spellDetail)
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(0.6f) // Используем weight для занимания доступного пространства внутри Column
                .padding(bottom = 16.dp) // Добавляем отступ снизу
        ) {
            Text(
                text = spellDetail.name.uppercase(),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = Utils.customFont_im_fell_english_regular,
                fontSize = MaterialTheme.typography.h6.fontSize * 0.60f,
                modifier = Modifier.padding(start = 10.dp)
            )

            DividerBox()
            Row(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 10.dp
                    )
                    .fillMaxWidth()
                    .scale(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                BottomBox(
                    textParameter = spellDetail.range,
                    iconParameter = R.drawable.icon_radius
                )
                BottomBox(
                    textParameter = spellDetail.casting_time,
                    iconParameter = R.drawable.icon_casting_time
                )
                BottomBox(
                    textParameter = spellDetail.duration,
                    iconParameter = R.drawable.icon_duration
                )
                BottomBox(
                    textParameter = spellDetail.components,
                    iconParameter = R.drawable.icon_components
                )
            }

        }
    }
}

/**
 * Метод, который отрисовывает Box над картинкой
 * с названием школы заклинания и списком классов, которые могут его изучить
 * @param spellDetail - информация о заклинании
 */
@Composable
fun TitleBox(spellDetail: SpellDetail) {
    val classes: MutableList<DndClass> = spellDetail.dnd_class.split(", ").mapNotNull {
            className ->
        val trimmedClassName = className.trim().toUpperCase(Locale.ROOT)
        val dndClass = try {
            DndClass.valueOf(trimmedClassName)
        } catch (e: IllegalArgumentException) {
            null
        }
        if (dndClass == null) {
            Log.e("MyApp", "Invalid DndClass value: $trimmedClassName")
        }
        dndClass
    }.toMutableList()
// Добавляем DndClass.EMPTY, если список classes оказался пустым
    if (classes.isEmpty()) {
        classes.add(DndClass.EMPTY)
    }
    Column(
        modifier = Modifier
            .padding(1.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 10.dp, end = 10.dp)
        ) {
            for (currentClass in classes) {
                val painter = ImageBitmap.imageResource(getIconResId(currentClass))
                Image(
                    bitmap = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .scale(1.3f) // Увеличиваем размер изображения
                        .padding(end = 10.dp)
                )
            }
        }
        Text(
            text = spellDetail.school.uppercase(),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Normal,
            fontFamily = Utils.customFont_im_fell_english_regular,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.h6.fontSize * 0.60f,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(), // Занимаем максимальную доступную ширину

        )
    }
}

/**
 * Метод, который отрисовывает Box с картинкой
 * @param spellDetail - информация о заклинании
 */
@Composable
fun ImageBox(spellDetail: SpellDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 0.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(0.dp)
                )
                .aspectRatio(0.90f) // Устанавливаем соотношение сторон для Box
        ) {
            Image(
                painter = painterResource(id = schoolCheck(spellDetail).first),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        // Добавляем вертикальный отступ между изображением и текстом
        Spacer(modifier = Modifier.height(6.dp))
    }
}

/**
Метод, который отрисовывает линию на карточке
 */
@Composable
fun DividerBox() {
    Row(
        modifier = Modifier
            .padding(end = 10.dp, start = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.White, CircleShape)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.White, CircleShape)
        )
    }
}

/**
 * Метод, который отрисовывает элементы, расположенные ниже линии
 * @param textParameter - текст, который будет отображаться перед иконкой
 * @param iconParameter - картинка параметра
 */
@Composable
fun BottomBox(textParameter: String, iconParameter: Int) {
    Row(
        modifier = Modifier
            .padding(end = 10.dp)

    ) {
        Image(
            painter = painterResource(id = iconParameter),
            contentDescription = null,
            modifier = Modifier
                .scale(1.4f) // Увеличиваем размер изображения
                .padding(end = 4.dp)
        )
        Text(
            text = checkDuration(isLongText(textParameter.uppercase())),
            style = MaterialTheme.typography.h6.copy(
                fontSize = MaterialTheme.typography.h6.fontSize * 0.35f
            ), // Уменьшаем размер шрифта
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Bottom)
        )
    }
}

