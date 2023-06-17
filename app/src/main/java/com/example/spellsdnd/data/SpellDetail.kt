package com.example.spellsdnd.data

import androidx.compose.runtime.MutableState


/**
 * Класс, который хранит информацию об отдельной карточке с заклинанием
 * @param slug - id заклинания
 * @param name - название заклинания
 * @param desc - описание заклинания
 * @param higher_level - доп.описание, только для высоких уровней
 * @param page - страница, на которой расположено заклинание
 * @param range - дальность применения
 * @param components - необходимые компоненты
 * @param material - необходимый материал
 * @param ritual - ритуал (да/нет)
 * @param duration - длительность
 * @param concentration - концентрация (да/нет)
 * @param casting_time - время накладывания заклинания
 * @param level - уровень
 * @param level_int - уровень (числовой тип)
 * @param school - школа заклинания
 * @param dnd_class - классы, которые могут изучить это заклинание
 * @param archetype - архетипы, которые могут изучить это заклинание
 * @param circles -
 * @param document__slug - id документа
 * @param document__title - Название документа
 * @param document__license_url - ссылка на документ с лицензией
 */
data class SpellDetail(
    var slug: String,
    var name: String,
    var desc: String,
    var higher_level: String,
    var page: String,
    var range: String,
    var components: String,
    var material: String,
    var ritual: String,
    var duration: String,
    var concentration: String,
    var casting_time: String,
    var level: String,
    val level_int: Int,
    var school: String,
    var dnd_class: String,
    var archetype: String,
    var circles: String,
    var document__slug: String,
    var document__title: String,
    var document__license_url: String,
)

data class SpellCreationState(
    val slug: MutableState<String>,
    val name: MutableState<String>,
    val desc: MutableState<String>,
    val higherLevel: MutableState<String>,
    val page: MutableState<String>,
    val range: MutableState<String>,
    val components: MutableState<String>,
    val material: MutableState<String>,
    val ritual: MutableState<String>,
    val duration: MutableState<String>,
    val concentration: MutableState<String>,
    val castingTime: MutableState<String>,
    val level: MutableState<String>,
    val levelInt: MutableState<Int>,
    val plug: MutableState<Int>, //заглушка
    val school: MutableState<String>,
    val dndClass: MutableState<String>,
    val archetype: MutableState<String>
)

