package com.example.spellsdnd.data


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
    val slug: String, // id spell
    val name: String, // name spell
    val desc: String, // description spell
    val higher_level: String, // it's desc spell if you use her on higher level
    val page: String,
    val range: String,
    val components: String,
    val material: String,
    val ritual: String,
    val duration: String,
    val concentration: String,
    val casting_time: String,
    val level: String,
    val level_int: Int,
    val school: String,
    val dnd_class: String,
    val archetype: String,
    val circles: String,
    val document__slug: String,
    val document__title: String,
    val document__license_url: String,
)

