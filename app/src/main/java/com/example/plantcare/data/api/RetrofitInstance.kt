package com.example.plantcare.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // TODO: Move to BuildConfig for production
    const val API_KEY = "sk-4leb69f6f08be1ff616942"

    private const val BASE_URL = "https://perenual.com/api/v2/"

    val api: PerenualApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PerenualApi::class.java)
    }
}
