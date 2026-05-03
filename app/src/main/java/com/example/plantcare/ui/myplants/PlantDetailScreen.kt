package com.example.plantcare.ui.myplants

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
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
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plantId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    plantViewModel: PlantViewModel
) {
    val plants by plantViewModel.plants.collectAsState()
    val plantState by plantViewModel.plantState.collectAsState()
    
    val plant = plants.find { it.id == plantId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Plant Details", 
                        fontWeight = FontWeight.Bold, 
                        color = PrimaryGreen,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(plantId) }) {
                        Icon(androidx.compose.material.icons.Icons.Filled.Edit, contentDescription = "Edit", tint = PrimaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (plant != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = { 
                            plantViewModel.deletePlant(plant.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(16.dp),
                        enabled = plantState !is PlantState.Loading
                    ) {
                        if (plantState is PlantState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Plant", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (plant == null) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Plant not found", color = TextGray)
                return@Column
            }

            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(SurfaceLightGreen)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ficus_elastica), // Fallback image for now
                    contentDescription = plant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = plant.name,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                
                if (plant.species.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plant.species,
                        fontSize = 18.sp,
                        color = TextGray
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(
                        title = "Watering",
                        value = plant.wateringInterval
                    )
                    DetailItem(
                        title = "Sunlight",
                        value = plant.sunlightNeeds
                    )
                }
                
                // Show Error if deletion failed
                if (plantState is PlantState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (plantState as PlantState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Navigate back when successfully deleted
        LaunchedEffect(plantState) {
            if (plantState is PlantState.Success) {
                plantViewModel.resetState()
                onNavigateBack()
            }
        }
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            color = TextGray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            color = PrimaryGreen,
            fontWeight = FontWeight.SemiBold
        )
    }
}
