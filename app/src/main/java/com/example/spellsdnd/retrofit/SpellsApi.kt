package com.example.spellsdnd.retrofit

import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.data.SpellResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SpellsApi {
    @GET("/spells") // Указываем эндпоинт для получения списка заклинаний
    // Метод для выполнения GET-запроса
    fun getSpells(@Query("page") page: Int): Call<SpellResponse>
}

object SpellManager {
    private const val BASE_URL = "https://api.open5e.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val spellsApi = retrofit.create(SpellsApi::class.java)
    fun getAllSpells(callback: (List<SpellDetail>?, Throwable?) -> Unit) {
        val spellsList = mutableListOf<SpellDetail>() // Создаем пустой список для заклинаний

        // Внутренняя функция, которая будет вызываться рекурсивно для получения следующей страницы
        fun getSpellsPage(page: Int) {
            val call = spellsApi.getSpells(page)
            call.enqueue(object : Callback<SpellResponse> {
                override fun onResponse(call: Call<SpellResponse>, response: Response<SpellResponse>) {
                    if (response.isSuccessful) {
                        val spellResponse = response.body()
                        val spells = spellResponse?.results ?: emptyList()
                        spellsList.addAll(spells) // Добавляем полученные заклинания в список
                        val nextPage = spellResponse?.next?.let { getNextPageFromUrl(it) }
                        if (nextPage != null) {
                            // Вызываем getSpellsPage рекурсивно для получения следующей страницы
                            getSpellsPage(nextPage)
                        } else {
                            // Если страниц больше нет, вызываем колбэк с окончательным списком заклинаний
                            callback(spellsList, null)
                        }
                    } else {
                        val error = Throwable("Ошибка при получении списка заклинаний")
                        callback(null, error)
                    }
                }
                override fun onFailure(call: Call<SpellResponse>, t: Throwable) {
                    callback(null, t)
                }
            })
        }
        getSpellsPage(1) // Запускаем запрос на получение первой страницы заклинаний
    }
    // Метод для извлечения значения параметра "page" из URL
    private fun getNextPageFromUrl(url: String): Int? {
        val regex = ".*[?&]page=(\\d+).*".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.getOrNull(1)?.toIntOrNull()
    }
}