package com.example.plantcare.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PerenualApi {

    @GET("species-list")
    suspend fun searchPlants(
        @Query("key") apiKey: String = RetrofitInstance.API_KEY,
        @Query("q") query: String,
        @Query("indoor") indoor: Int? = null
    ): SpeciesListResponse

    @GET("species/details/{id}")
    suspend fun getPlantDetails(
        @Path("id") id: Int,
        @Query("key") apiKey: String = RetrofitInstance.API_KEY
    ): SpeciesDetailResponse
}
