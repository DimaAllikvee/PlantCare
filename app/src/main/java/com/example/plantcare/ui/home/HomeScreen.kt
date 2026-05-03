package com.example.plantcare.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.plantcare.ui.navigation.PlantCareBottomNavigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.myplants.PlantViewModel
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@Composable
fun HomeScreen(
    onNavigateToPlants: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    plantViewModel: PlantViewModel
) {
    val plants by plantViewModel.plants.collectAsState()

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { if (plants.isEmpty()) 1 else plants.size })

    Scaffold(
        bottomBar = {
            PlantCareBottomNavigation(
                currentRoute = "home",
                onNavigateToHome = { /* Already here */ },
                onNavigateToCalendar = onNavigateToCalendar,
                onNavigateToPlants = onNavigateToPlants,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F2F4))
                .padding(paddingValues)
        ) {
            if (plants.isEmpty()) {
                // Render Empty State
                Image(
                    painter = painterResource(id = R.drawable.ficus_elastica),
                    contentDescription = "Empty Background",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                
                HomeCard(
                    plant = null,
                    plantViewModel = plantViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(top = 280.dp)
                )
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val plant = plants[page]
                    
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Plant Image
                        Image(
                            painter = painterResource(id = R.drawable.ficus_elastica),
                            contentDescription = plant.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(330.dp),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                        
                        // Home Card Overlapping the Image
                        HomeCard(
                            plant = plant,
                            plantViewModel = plantViewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(top = 280.dp)
                        )
                    }
                }
                
                // Pager Indicators (Fractional Pill)
                if (plants.size > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 220.dp, end = 24.dp), // Position on the right side above the card
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${pagerState.currentPage + 1} / ${plants.size}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
