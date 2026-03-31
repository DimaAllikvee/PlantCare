package com.example.plantcare.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object MyPlants : Screen("my_plants")
    object AddNewPlant : Screen("add_new_plant")
}
