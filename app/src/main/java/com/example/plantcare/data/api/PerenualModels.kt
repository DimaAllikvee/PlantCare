package com.example.plantcare.data.api

import com.google.gson.annotations.SerializedName

// --- Species List (Search) ---
data class SpeciesListResponse(
    val data: List<SpeciesListItem>,
    val total: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int
)

data class SpeciesListItem(
    val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    @SerializedName("other_name") val otherName: List<String>?,
    val cycle: String?,
    val watering: String?,
    val sunlight: List<String>?,
    @SerializedName("default_image") val defaultImage: DefaultImage?
)

data class DefaultImage(
    @SerializedName("original_url") val originalUrl: String?,
    @SerializedName("regular_url") val regularUrl: String?,
    @SerializedName("medium_url") val mediumUrl: String?,
    @SerializedName("small_url") val smallUrl: String?,
    val thumbnail: String?
)

// --- Species Details ---
data class SpeciesDetailResponse(
    val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    val type: String?,
    val cycle: String?,
    val watering: String?,
    @SerializedName("watering_general_benchmark") val wateringBenchmark: WateringBenchmark?,
    val sunlight: List<String>?,
    @SerializedName("care_level") val careLevel: String?,
    @SerializedName("growth_rate") val growthRate: String?,
    val maintenance: String?,
    val indoor: Boolean?,
    val description: String?,
    @SerializedName("default_image") val defaultImage: DefaultImage?
)

data class WateringBenchmark(
    val value: String?,
    val unit: String?
)
