package com.example.plantcare.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // TODO: Move to BuildConfig for production
    const val API_KEY = "sk-JNKL67c109c3ae0e88442"

    private const val BASE_URL = "https://perenual.com/api/v2/"

    val api: PerenualApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PerenualApi::class.java)
    }
}
