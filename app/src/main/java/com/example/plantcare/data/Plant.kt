package com.example.plantcare.data

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val wateringInterval: String = "",
    val mistingInterval: String = "None",
    val fertilizingInterval: String = "None",
    val sunlightNeeds: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val lastWatered: Long = System.currentTimeMillis(),
    val lastMisted: Long = System.currentTimeMillis(),
    val lastFertilized: Long = System.currentTimeMillis(),
    val nextWateringOverride: Long? = null,
    val nextMistingOverride: Long? = null,
    val nextFertilizingOverride: Long? = null
)
