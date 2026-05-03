package com.example.plantcare.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantcare.ui.myplants.PlantViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.plantcare.ui.navigation.PlantCareBottomNavigation
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.TextGray
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.example.plantcare.data.Plant

data class TaskCardInfo(
    val plant: Plant,
    val taskType: String,
    val isCompleted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareCalendarScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToPlants: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    plantViewModel: PlantViewModel = viewModel()
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var taskToReschedule by remember { mutableStateOf<TaskCardInfo?>(null) }
    
    // Generate next 14 days for the selector
    val dates = remember { 
        (0..14).map { LocalDate.now().plusDays(it.toLong()) } 
    }
    
    val plants by plantViewModel.plants.collectAsState()
    
    // Calculate tasks for the selected date
    val tasksForSelectedDate = remember(plants, selectedDate) {
        val tasks = mutableListOf<TaskCardInfo>()
        val today = LocalDate.now()
        
        plants.forEach { plant ->
            // --- Watering ---
            if (plant.nextWateringOverride != null) {
                val overrideDate = Instant.ofEpochMilli(plant.nextWateringOverride).atZone(ZoneId.systemDefault()).toLocalDate()
                if (overrideDate == selectedDate) {
                    tasks.add(TaskCardInfo(plant, "Watering", false))
                }
            } else {
                val lastWateredDate = Instant.ofEpochMilli(plant.lastWatered).atZone(ZoneId.systemDefault()).toLocalDate()
                val wateringInterval = try { "\\d+".toRegex().find(plant.wateringInterval)?.value?.toInt() ?: 7 } catch (e: Exception) { 7 }
                val daysSinceWatered = ChronoUnit.DAYS.between(lastWateredDate, selectedDate).toInt()
                
                if (daysSinceWatered == 0) {
                    tasks.add(TaskCardInfo(plant, "Watering", true))
                } else if (daysSinceWatered > 0) {
                    val isDue = daysSinceWatered % wateringInterval == 0
                    val isOverdue = daysSinceWatered > wateringInterval
                    if (isDue || (selectedDate == today && isOverdue)) {
                        tasks.add(TaskCardInfo(plant, "Watering", false))
                    }
                }
            }
            
            // --- Misting ---
            if (plant.mistingInterval != "None" && plant.mistingInterval.isNotBlank()) {
                if (plant.nextMistingOverride != null) {
                    val overrideDate = Instant.ofEpochMilli(plant.nextMistingOverride).atZone(ZoneId.systemDefault()).toLocalDate()
                    if (overrideDate == selectedDate) {
                        tasks.add(TaskCardInfo(plant, "Misting", false))
                    }
                } else {
                    val lastMistedDate = Instant.ofEpochMilli(plant.lastMisted).atZone(ZoneId.systemDefault()).toLocalDate()
                    val mistingInterval = try { "\\d+".toRegex().find(plant.mistingInterval)?.value?.toInt() ?: 7 } catch (e: Exception) { 7 }
                    val daysSinceMisted = ChronoUnit.DAYS.between(lastMistedDate, selectedDate).toInt()
                    
                    if (daysSinceMisted == 0) {
                        tasks.add(TaskCardInfo(plant, "Misting", true))
                    } else if (daysSinceMisted > 0) {
                        val isDue = daysSinceMisted % mistingInterval == 0
                        val isOverdue = daysSinceMisted > mistingInterval
                        if (isDue || (selectedDate == today && isOverdue)) {
                            tasks.add(TaskCardInfo(plant, "Misting", false))
                        }
                    }
                }
            }
            
            // --- Fertilizing ---
            if (plant.fertilizingInterval != "None" && plant.fertilizingInterval.isNotBlank()) {
                if (plant.nextFertilizingOverride != null) {
                    val overrideDate = Instant.ofEpochMilli(plant.nextFertilizingOverride).atZone(ZoneId.systemDefault()).toLocalDate()
                    if (overrideDate == selectedDate) {
                        tasks.add(TaskCardInfo(plant, "Fertilizing", false))
                    }
                } else {
                    val lastFertilizedDate = Instant.ofEpochMilli(plant.lastFertilized).atZone(ZoneId.systemDefault()).toLocalDate()
                    val fertilizingInterval = try { "\\d+".toRegex().find(plant.fertilizingInterval)?.value?.toInt() ?: 7 } catch (e: Exception) { 7 }
                    val daysSinceFertilized = ChronoUnit.DAYS.between(lastFertilizedDate, selectedDate).toInt()
                    
                    if (daysSinceFertilized == 0) {
                        tasks.add(TaskCardInfo(plant, "Fertilizing", true))
                    } else if (daysSinceFertilized > 0) {
                        val isDue = daysSinceFertilized % fertilizingInterval == 0
                        val isOverdue = daysSinceFertilized > fertilizingInterval
                        if (isDue || (selectedDate == today && isOverdue)) {
                            tasks.add(TaskCardInfo(plant, "Fertilizing", false))
                        }
                    }
                }
            }
        }
        tasks
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Care Calendar",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        bottomBar = {
            PlantCareBottomNavigation(
                currentRoute = "calendar",
                onNavigateToHome = onNavigateToHome,
                onNavigateToCalendar = { /* Already here */ },
                onNavigateToPlants = onNavigateToPlants,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Month Header
            val monthYear = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH))
            Text(
                text = monthYear,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Horizontal Date Selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(dates.size) { index ->
                    val date = dates[index]
                    val isSelected = date == selectedDate
                    
                    DateCard(
                        date = date,
                        isSelected = isSelected,
                        onClick = { selectedDate = date }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Tasks Section Title
            Text(
                text = "Tasks for Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Current Date Subtitle
            val fullDateStr = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH))
            Text(
                text = fullDateStr,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TextGray,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Task List
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (tasksForSelectedDate.isEmpty()) {
                    item {
                        Text(
                            text = "No tasks for this day! Relax and enjoy.",
                            color = TextGray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                } else {
                    items(tasksForSelectedDate.size) { index ->
                        val taskInfo = tasksForSelectedDate[index]
                        
                        // We use a unique key for TaskCard to force recomposition if completed state changes
                        key(taskInfo.plant.id, taskInfo.taskType, taskInfo.isCompleted) {
                            TaskCard(
                                plantName = taskInfo.plant.name,
                                taskType = taskInfo.taskType,
                                isCompleted = taskInfo.isCompleted,
                                onToggle = {
                                    if (!taskInfo.isCompleted) {
                                        // Mark as done in Firebase based on type
                                        when (taskInfo.taskType) {
                                            "Watering" -> plantViewModel.markPlantWatered(taskInfo.plant.id)
                                            "Misting" -> plantViewModel.markPlantMisted(taskInfo.plant.id)
                                            "Fertilizing" -> plantViewModel.markPlantFertilized(taskInfo.plant.id)
                                        }
                                    }
                                },
                                onRescheduleClick = {
                                    taskToReschedule = taskInfo
                                }
                            )
                        }
                    }
                }
            }
            
            if (taskToReschedule != null) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = Instant.now().toEpochMilli()
                )
                
                DatePickerDialog(
                    onDismissRequest = { taskToReschedule = null },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val task = taskToReschedule!!
                                plantViewModel.rescheduleTask(task.plant.id, task.taskType, millis)
                            }
                            taskToReschedule = null
                        }) {
                            Text("OK", color = PrimaryGreen)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { taskToReschedule = null }) {
                            Text("Cancel", color = TextGray)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
fun DateCard(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayOfWeek = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH).uppercase()
    val dayOfMonth = date.dayOfMonth.toString()
    
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
    val subTextColor = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .width(64.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dayOfWeek,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = subTextColor
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dayOfMonth,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun TaskCard(
    plantName: String,
    taskType: String,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onRescheduleClick: () -> Unit
) {
    var completed by remember { mutableStateOf(isCompleted) }
    
    val icon = when {
        taskType.contains("Water", ignoreCase = true) -> Icons.Outlined.WaterDrop
        taskType.contains("Fertiliz", ignoreCase = true) -> Icons.Outlined.WbSunny // Placeholder for leaf
        else -> Icons.Outlined.WbSunny // Placeholder for spray
    }
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                completed = !completed
                onToggle() 
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // 1. Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (completed) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (completed) Color.Transparent else MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 2. Circular Plant Image
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // In a real app with AsyncImage, we'd use plant.imageUrl
                // For now we use the placeholder
                Image(
                    painter = painterResource(id = R.drawable.ficus_elastica),
                    contentDescription = plantName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 3. Text Column
            Column {
                Text(
                    text = plantName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                    textDecoration = if (completed) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = taskType,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = taskType,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (!completed) {
                IconButton(onClick = onRescheduleClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Reschedule",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
