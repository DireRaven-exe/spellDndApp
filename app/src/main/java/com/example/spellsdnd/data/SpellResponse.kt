package com.example.spellsdnd.data

/**
 * Класс, который хранит список заклинаний и ссылку (URL) на следующую страницу с карточками
 * @param results - список карточек
 * @param next - следующая страница с карточками
 */
data class SpellResponse(
    val results: List<SpellDetail>,
    val next: String
)

