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
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPlantScreen(
    plantId: String? = null,
    onNavigateBack: () -> Unit = {},
    plantViewModel: PlantViewModel = viewModel(),
    catalogViewModel: PlantCatalogViewModel = viewModel()
) {
    val plantState by plantViewModel.plantState.collectAsState()
    val plants by plantViewModel.plants.collectAsState()
    val isEditMode = plantId != null

    val searchResults by catalogViewModel.searchResults.collectAsState()
    val isSearching by catalogViewModel.isSearching.collectAsState()
    val selectedDetails by catalogViewModel.selectedPlantDetails.collectAsState()
    val isLoadingDetails by catalogViewModel.isLoadingDetails.collectAsState()

    var plantName by remember { mutableStateOf("") }
    var plantSpecies by remember { mutableStateOf("") }
    var wateringInterval by remember { mutableStateOf("") }
    var mistingInterval by remember { mutableStateOf("None") }
    var fertilizingInterval by remember { mutableStateOf("None") }
    var selectedSunlight by remember { mutableStateOf("Medium") }
    var selectedImageUrl by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }
    
    LaunchedEffect(plantId, plants) {
        if (isEditMode) {
            val existingPlant = plants.find { it.id == plantId }
            if (existingPlant != null) {
                plantName = existingPlant.name
                plantSpecies = existingPlant.species
                wateringInterval = existingPlant.wateringInterval
                mistingInterval = existingPlant.mistingInterval
                fertilizingInterval = existingPlant.fertilizingInterval
                selectedSunlight = existingPlant.sunlightNeeds
                selectedImageUrl = existingPlant.imageUrl
            }
        }
    }

    // Auto-fill when details arrive from API
    LaunchedEffect(selectedDetails) {
        selectedDetails?.let { details ->
            details.commonName?.let { 
                plantSpecies = it 
                if (plantName.isBlank()) {
                    plantName = it.replaceFirstChar { char -> char.uppercase() }
                }
            }
            selectedSunlight = catalogViewModel.mapSunlight(details.sunlight)
            val interval = catalogViewModel.mapWateringToInterval(
                details.watering,
                details.wateringBenchmark?.value
            )
            wateringInterval = interval
            val imgUrl = details.defaultImage?.regularUrl 
                ?: details.defaultImage?.mediumUrl 
                ?: details.defaultImage?.originalUrl 
                ?: details.defaultImage?.smallUrl 
                ?: details.defaultImage?.thumbnail
            if (!imgUrl.isNullOrEmpty()) {
                selectedImageUrl = imgUrl
            }
            catalogViewModel.clearSelectedDetails()
        }
    }
    
    var expandedWatering by remember { mutableStateOf(false) }
    var expandedMisting by remember { mutableStateOf(false) }
    var expandedFertilizing by remember { mutableStateOf(false) }
    
    val wateringOptions = listOf("Every 1 day", "Every 2 days", "Every 3 days", "Every 5 days", "Every 7 days", "Every 14 days")
    val mistingOptions = listOf("None", "Every 1 day", "Every 2 days", "Every 3 days", "Every 7 days")
    val fertilizingOptions = listOf("None", "Every 7 days", "Every 14 days", "Every 30 days")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (isEditMode) "Edit Plant" else "Add New Plant", 
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
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
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
                            if (isEditMode) {
                                plantViewModel.updatePlant(
                                    plantId = plantId!!,
                                    name = plantName,
                                    species = plantSpecies,
                                    interval = wateringInterval.ifEmpty { "Every 7 days" },
                                    mistingInterval = mistingInterval,
                                    fertilizingInterval = fertilizingInterval,
                                    sunlight = selectedSunlight,
                                    imageUrl = selectedImageUrl
                                )
                            } else {
                                plantViewModel.addPlant(
                                    name = plantName,
                                    species = plantSpecies,
                                    interval = wateringInterval.ifEmpty { "Every 7 days" },
                                    mistingInterval = mistingInterval,
                                    fertilizingInterval = fertilizingInterval,
                                    sunlight = selectedSunlight,
                                    imageUrl = selectedImageUrl
                                )
                            }
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
                            Text(if (isEditMode) "Save Changes" else "Save Plant", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(SurfaceLightGreen)
                    .clickable { /* Handle photo upload */ }
            ) {
                if (selectedImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = selectedImageUrl,
                        contentDescription = "Plant Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = "Upload Photo", tint = PrimaryGreen, modifier = Modifier.size(32.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                if (isEditMode) "Change Plant Photo" else "Upload Plant Photo",
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
                onValueChange = { 
                    plantSpecies = it
                    showSuggestions = true
                    catalogViewModel.searchSpecies(it)
                },
                placeholder = "Search species...",
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextGray) }
            )

            // Search Suggestions Dropdown
            if (showSuggestions && (searchResults.isNotEmpty() || isSearching)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    val errorMessage by catalogViewModel.errorMessage.collectAsState()
                    
                    if (errorMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else if (isSearching) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryGreen,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        LazyColumn {
                            items(searchResults) { species ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            plantSpecies = species.commonName ?: ""
                                            showSuggestions = false
                                            catalogViewModel.clearSearch()
                                            // Fetch full details for auto-fill
                                            catalogViewModel.getPlantDetails(species.id)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Thumbnail
                                    val thumbUrl = species.defaultImage?.thumbnail
                                        ?: species.defaultImage?.smallUrl
                                        ?: species.defaultImage?.regularUrl
                                        ?: species.defaultImage?.mediumUrl
                                        ?: species.defaultImage?.originalUrl
                                    if (thumbUrl != null) {
                                        AsyncImage(
                                            model = thumbUrl,
                                            contentDescription = species.commonName,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SurfaceLightGreen)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SurfaceLightGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🌱", fontSize = 20.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = species.commonName ?: "Unknown",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = PrimaryGreen,
                                            maxLines = 1
                                        )
                                        val sciName = species.scientificName?.firstOrNull()
                                        if (sciName != null) {
                                            Text(
                                                text = sciName,
                                                fontSize = 11.sp,
                                                color = TextGray,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    // Watering badge
                                    species.watering?.let { watering ->
                                        Text(
                                            text = watering,
                                            fontSize = 10.sp,
                                            color = PrimaryGreen,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .background(SurfaceLightGreen, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Loading details indicator
            if (isLoadingDetails) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Auto-filling plant data...", fontSize = 12.sp, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = expandedWatering,
                onExpandedChange = { expandedWatering = !expandedWatering }
            ) {
                PlantInputField(
                    label = "Watering Interval",
                    value = wateringInterval.ifEmpty { "Every 7 days" },
                    onValueChange = {},
                    placeholder = "Every 7 days",
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedWatering) },
                    readOnly = true,
                    textFieldModifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedWatering,
                    onDismissRequest = { expandedWatering = false },
                    modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    wateringOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = PrimaryGreen) },
                            onClick = {
                                wateringInterval = selectionOption
                                expandedWatering = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = expandedMisting,
                onExpandedChange = { expandedMisting = !expandedMisting }
            ) {
                PlantInputField(
                    label = "Misting Interval",
                    value = mistingInterval,
                    onValueChange = {},
                    placeholder = "None",
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMisting) },
                    readOnly = true,
                    textFieldModifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedMisting,
                    onDismissRequest = { expandedMisting = false },
                    modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    mistingOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = PrimaryGreen) },
                            onClick = {
                                mistingInterval = selectionOption
                                expandedMisting = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = expandedFertilizing,
                onExpandedChange = { expandedFertilizing = !expandedFertilizing }
            ) {
                PlantInputField(
                    label = "Fertilizing Interval",
                    value = fertilizingInterval,
                    onValueChange = {},
                    placeholder = "None",
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFertilizing) },
                    readOnly = true,
                    textFieldModifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .height(56.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedFertilizing,
                    onDismissRequest = { expandedFertilizing = false },
                    modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    fertilizingOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = PrimaryGreen) },
                            onClick = {
                                fertilizingInterval = selectionOption
                                expandedFertilizing = false
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
                    .border(1.dp, androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surface),
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
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight(0.6f).background(androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant))
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
                unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryGreen,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}
