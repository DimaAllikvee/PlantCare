package com.example.plantcare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.ui.home.HomeScreen
import com.example.plantcare.ui.myplants.MyPlantsScreen
import com.example.plantcare.ui.newplant.AddNewPlantScreen
import com.example.plantcare.ui.signup.SignUpScreen
import com.example.plantcare.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            // Pass navigation callback
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        // Clear login from back stack
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onLoginClick = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPlants = {
                    navController.navigate(Screen.MyPlants.route)
                }
            )
        }
        
        composable(Screen.MyPlants.route) {
            MyPlantsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToAddNewPlant = {
                    navController.navigate(Screen.AddNewPlant.route)
                }
            )
        }
        
        composable(Screen.AddNewPlant.route) {
            AddNewPlantScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
