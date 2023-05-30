package com.example.spellsdnd.retrofit

import com.example.spellsdnd.data.SpellDetail
import com.example.spellsdnd.data.SpellResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface SpellsRusApi {
    @GET("DireRaven-exe/spell-request-json/master/request/spells.json")
    fun getRusSpells(): Call<SpellResponse>
}


object SpellRusManager {
    private const val BASE_URL = "https://raw.githubusercontent.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val spellsRuApi = retrofit.create(SpellsRusApi::class.java)

    fun getAllRusSpells(callback: (List<SpellDetail>?, Throwable?) -> Unit) {
        val spellsList = mutableListOf<SpellDetail>() // Создаем пустой список для заклинаний
        val call = spellsRuApi.getRusSpells()
        call.enqueue(object : Callback<SpellResponse> {
            override fun onResponse(call: Call<SpellResponse>, response: Response<SpellResponse>) {
                if (response.isSuccessful) {
                    val spellResponse = response.body()
                    val spells = spellResponse?.results ?: emptyList()
                    spellsList.addAll(spells) // Добавляем полученные заклинания в список
                    callback(spells, null)
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
}

