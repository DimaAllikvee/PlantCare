package com.example.plantcare.ui.myplants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import com.example.plantcare.ui.navigation.PlantCareBottomNavigation
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantcare.ui.myplants.PlantViewModel
import com.example.plantcare.data.Plant
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlantsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToAddNewPlant: () -> Unit = {},
    onNavigateToPlantDetail: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    plantViewModel: PlantViewModel = viewModel()
) {
    val plants by plantViewModel.plants.collectAsState()
    
    // Automatically fetch plants when screen is launched
    LaunchedEffect(Unit) {
        plantViewModel.fetchPlants()
    }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            PlantCareBottomNavigation(
                currentRoute = "my_plants",
                onNavigateToHome = onNavigateToHome,
                onNavigateToCalendar = onNavigateToCalendar,
                onNavigateToPlants = { /* Already here */ },
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Header Row with Title and Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Plants",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                
                IconButton(
                    onClick = onNavigateToAddNewPlant,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryGreen)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add New Plant", tint = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Search plants...", color = TextGray) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CardBackground,
                    focusedContainerColor = CardBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = PrimaryGreen,
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Vertical List of Plants
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (plants.isEmpty()) {
                    item {
                        Text(
                            text = "No plants added yet. Click + to add one!", 
                            color = TextGray, 
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(plants.size) { index ->
                        val plant = plants[index]
                        
                        val diffMs = System.currentTimeMillis() - plant.lastWatered
                        val daysSince = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
                        
                        val intervalMs: Int = try {
                            val regex = "\\d+".toRegex()
                            val match = regex.find(plant.wateringInterval)
                            match?.value?.toInt() ?: 7
                        } catch (e: Exception) {
                            7
                        }
                        
                        val isDue = daysSince >= intervalMs
                        
                        val lastWateredText = when (daysSince) {
                            0 -> "Today"
                            1 -> "Yesterday"
                            else -> "$daysSince days ago"
                        }
                        val statusString = if (isDue) "Needs Water" else "Watered"

                        PlantListCard(
                            name = plant.name,
                            lastWatered = lastWateredText,
                            statusText = statusString,
                            isDue = isDue,
                            imageUrl = plant.imageUrl,
                            onClick = { onNavigateToPlantDetail(plant.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlantListCard(
    name: String,
    lastWatered: String,
    statusText: String,
    isDue: Boolean,
    imageUrl: String = "",
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Image
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceLightGreen)
            ) {
                if (imageUrl.isNotEmpty()) {
                    coil.compose.AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imagePainter = painterResource(id = R.drawable.ficus_elastica)
                    Image(
                        painter = imagePainter,
                        contentDescription = name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Middle Text Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PrimaryGreen,
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Last watered:\n$lastWatered",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Right Status Badge
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDue) Color(0xFFFFF3E0) else androidx.compose.material3.MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isDue) Icons.Outlined.Schedule else Icons.Outlined.WaterDrop,
                        contentDescription = statusText,
                        tint = if (isDue) Color(0xFFFF9800) else androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDue) Color(0xFFFF9800) else androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
