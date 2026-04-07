package com.example.plantcare.data

data class Plant(
    val id: String = "",
    val name: String = "",
    val species: String = "",
    val wateringInterval: String = "",
    val sunlightNeeds: String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val lastWatered: Long = System.currentTimeMillis()
)
