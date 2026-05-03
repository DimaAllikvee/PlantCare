package com.example.plantcare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantcare.ui.home.HomeScreen
import com.example.plantcare.ui.myplants.MyPlantsScreen
import com.example.plantcare.ui.myplants.PlantDetailScreen
import com.example.plantcare.ui.myplants.PlantViewModel
import com.example.plantcare.ui.newplant.AddNewPlantScreen
import com.example.plantcare.ui.signup.SignUpScreen
import com.example.plantcare.ui.login.LoginScreen
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Shared ViewModel for plant management across the app flow
    val sharedPlantViewModel: PlantViewModel = viewModel()

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
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route)
                },
                plantViewModel = sharedPlantViewModel
            )
        }
        
        composable(Screen.MyPlants.route) {
            MyPlantsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route)
                },
                onNavigateToAddNewPlant = {
                    navController.navigate(Screen.AddNewPlant.route)
                },
                onNavigateToPlantDetail = { plantId ->
                    navController.navigate(Screen.PlantDetail.createRoute(plantId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                plantViewModel = sharedPlantViewModel
            )
        }
        
        composable(Screen.AddNewPlant.route) {
            AddNewPlantScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                plantViewModel = sharedPlantViewModel
            )
        }

        composable(Screen.PlantDetail.route) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")
            if (plantId != null) {
                PlantDetailScreen(
                    plantId = plantId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = { editPlantId ->
                        navController.navigate(Screen.EditPlant.createRoute(editPlantId))
                    },
                    plantViewModel = sharedPlantViewModel
                )
            }
        }

        composable(Screen.EditPlant.route) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")
            if (plantId != null) {
                AddNewPlantScreen(
                    plantId = plantId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    plantViewModel = sharedPlantViewModel
                )
            }
        }

        composable(Screen.Profile.route) {
            com.example.plantcare.ui.profile.ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToPlants = {
                    navController.navigate(Screen.MyPlants.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                plantViewModel = sharedPlantViewModel
            )
        }
        
        composable(Screen.Calendar.route) {
            com.example.plantcare.ui.calendar.CareCalendarScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToPlants = { navController.navigate(Screen.MyPlants.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                plantViewModel = sharedPlantViewModel
            )
        }
    }
}
