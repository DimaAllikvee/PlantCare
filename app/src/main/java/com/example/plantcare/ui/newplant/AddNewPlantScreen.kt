package com.example.plantcare.ui.newplant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantcare.ui.myplants.PlantViewModel
import com.example.plantcare.ui.myplants.PlantState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPlantScreen(
    onNavigateBack: () -> Unit = {},
    plantViewModel: PlantViewModel = viewModel()
) {
    val plantState by plantViewModel.plantState.collectAsState()
    var plantName by remember { mutableStateOf("") }
    var plantSpecies by remember { mutableStateOf("") }
    var wateringInterval by remember { mutableStateOf("") }
    var selectedSunlight by remember { mutableStateOf("Medium") }
    var expanded by remember { mutableStateOf(false) }
    val intervalOptions = listOf("Every 1 day", "Every 2 days", "Every 3 days", "Every 5 days", "Every 7 days", "Every 14 days")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Add New Plant", 
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF1F2F4)
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F2F4))
                    .padding(24.dp)
            ) {
                Column {
                    if (plantState is PlantState.Error) {
                        Text(
                            text = (plantState as PlantState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = { 
                            plantViewModel.addPlant(
                                name = plantName,
                                species = plantSpecies,
                                interval = wateringInterval.ifEmpty { "Every 7 days" }, // default if missed typing
                                sunlight = selectedSunlight,
                                imageUrl = "" // Fallback to empty string for default photo logic
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(16.dp),
                        enabled = plantState !is PlantState.Loading
                    ) {
                        if (plantState is PlantState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Save Plant", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }

            LaunchedEffect(plantState) {
                if (plantState is PlantState.Success) {
                    plantViewModel.resetState()
                    onNavigateBack()
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Upload Photo Section
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(SurfaceLightGreen)
                    .clickable { /* Handle photo upload */ }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Upload Photo", tint = PrimaryGreen, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Upload Plant Photo",
                color = PrimaryGreen,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            PlantInputField(
                label = "Plant Name",
                value = plantName,
                onValueChange = { plantName = it },
                placeholder = "e.g., Monty"
            )

            Spacer(modifier = Modifier.height(20.dp))

            PlantInputField(
                label = "Plant Species",
                value = plantSpecies,
                onValueChange = { plantSpecies = it },
                placeholder = "Search species...",
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextGray) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                PlantInputField(
                    label = "Watering Interval",
                    value = wateringInterval.ifEmpty { "Every 7 days" },
                    onValueChange = {},
                    placeholder = "Every 7 days",
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    readOnly = true,
                    textFieldModifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(CardBackground)
                ) {
                    intervalOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = PrimaryGreen) },
                            onClick = {
                                wateringInterval = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sunlight Needs Segmented Control
            Text(
                text = "Sunlight Needs",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = PrimaryGreen,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val sunlightOptions = listOf("Low", "Medium", "High")
                sunlightOptions.forEachIndexed { index, option ->
                    val isSelected = selectedSunlight == option
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(
                                RoundedCornerShape(
                                    topStart = if (index == 0) 12.dp else 0.dp,
                                    bottomStart = if (index == 0) 12.dp else 0.dp,
                                    topEnd = if (index == 2) 12.dp else 0.dp,
                                    bottomEnd = if (index == 2) 12.dp else 0.dp
                                )
                            )
                            .background(if (isSelected) PrimaryGreen else Color.Transparent)
                            .clickable { selectedSunlight = option }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (option == "Medium") {
                                Icon(Icons.Outlined.WbSunny, contentDescription = option, tint = if (isSelected) Color.White else PrimaryGreen, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = option,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else PrimaryGreen,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Vertical Divider
                    if (index < 2) {
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight(0.6f).background(Color(0xFFE0E0E0)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PlantInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    textFieldModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = PrimaryGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = textFieldModifier,
            placeholder = { Text(placeholder, color = TextGray) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = CardBackground,
                focusedContainerColor = CardBackground,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryGreen,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}
