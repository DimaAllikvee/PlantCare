package com.example.plantcare.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@Composable
fun PlantCareBottomNavigation(
    currentRoute: String?,
    onNavigateToHome: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToPlants: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val items = listOf("Home", "Calendar", "Plants", "Profile")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.LocalFlorist, Icons.Filled.Person)
    
    // Map items to their corresponding routes
    val routeMapping = mapOf(
        "Home" to Screen.Home.route,
        "Calendar" to "calendar", // Screen.Calendar.route
        "Plants" to Screen.MyPlants.route,
        "Profile" to "profile" // We'll assume Screen.Profile.route is "profile" based on general convention
    )

    NavigationBar(
        containerColor = SurfaceLightGreen,
        contentColor = PrimaryGreen,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            // Determine if selected based on current route mapping
            val isSelected = currentRoute == routeMapping[item] || 
                             // Fallback for hardcoded views that don't pass route correctly
                             (currentRoute == null && 
                               ((item == "Home" && index == 0) || 
                                (item == "Calendar" && index == 1) || 
                                (item == "Plants" && index == 2) || 
                                (item == "Profile" && index == 3)))

            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item, fontWeight = FontWeight.Medium) },
                selected = isSelected,
                onClick = {
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToCalendar()
                        2 -> onNavigateToPlants()
                        3 -> onNavigateToProfile()
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryGreen.copy(alpha = 0.2f),
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray
                )
            )
        }
    }
}
