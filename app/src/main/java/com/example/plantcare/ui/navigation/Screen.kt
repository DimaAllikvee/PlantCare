package com.example.plantcare.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object MyPlants : Screen("my_plants")
    object AddNewPlant : Screen("add_new_plant")
    object PlantDetail : Screen("plant_detail/{plantId}") {
        fun createRoute(plantId: String) = "plant_detail/$plantId"
    }
    object Profile : Screen("profile")
}
