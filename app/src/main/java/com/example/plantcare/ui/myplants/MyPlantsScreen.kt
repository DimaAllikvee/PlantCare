package com.example.plantcare.ui.myplants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onNavigateToAddNewPlant: () -> Unit = {}
) {
    var selectedItem by remember { mutableStateOf(2) }
    var searchQuery by remember { mutableStateOf("") }
    val items = listOf("Home", "Calendar", "Plants")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.LocalFlorist)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceLightGreen,
                contentColor = PrimaryGreen
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item, fontWeight = FontWeight.Medium) },
                        selected = selectedItem == index,
                        onClick = { 
                            selectedItem = index
                            if (index == 0) {
                                onNavigateToHome()
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F2F4))
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
                items(6) { index ->
                    PlantListCard(
                        name = if (index % 2 == 0) "Monstera Deliciosa" else "Golden Pothos",
                        lastWatered = "5 days ago",
                        statusText = if (index % 2 == 0) "Watered" else "Due Today",
                        isDue = index % 2 != 0
                    )
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
    isDue: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
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
                Image(
                    painter = painterResource(id = R.drawable.ficus_elastica), // Placeholder image
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                        .background(if (isDue) Color(0xFFFFF3E0) else SurfaceLightGreen)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isDue) Icons.Outlined.Schedule else Icons.Outlined.WaterDrop,
                        contentDescription = statusText,
                        tint = if (isDue) Color(0xFFFF9800) else PrimaryGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDue) Color(0xFFFF9800) else PrimaryGreen
                    )
                }
            }
        }
    }
}
