package com.example.plantcare.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.data.Plant
import com.example.plantcare.ui.myplants.PlantViewModel
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.TextGray
import com.example.plantcare.ui.theme.WidgetBackground
import com.example.plantcare.ui.theme.WidgetText
import java.util.concurrent.TimeUnit

@Composable
fun HomeWidget(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(100.dp))
            .height(32.dp)
            .padding(horizontal = 12.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MoistureChart(
    percentage: Int,
    modifier: Modifier = Modifier
) {
    val animatedPercent by animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(durationMillis = 1500),
        label = "moisture_anim"
    )

    val arcColor = when {
        animatedPercent > 0.4f -> androidx.compose.material3.MaterialTheme.colorScheme.primary
        animatedPercent > 0.2f -> Color(0xFFFF9800) // Orange warning
        else -> Color(0xFFE53935) // Red alert!
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(170.dp)
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            // Draw background track
            drawArc(
                color = Color(0xFFCDE0D5), 
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // Draw progress arc
            drawArc(
                color = arcColor,
                startAngle = 135f,
                sweepAngle = 270f * animatedPercent,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(animatedPercent * 100).toInt()}%",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Moisture Level",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                imageVector = Icons.Outlined.WaterDrop,
                contentDescription = null,
                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun HomeCard(
    plant: Plant?,
    plantViewModel: PlantViewModel?,
    modifier: Modifier = Modifier
) {
    val plantName = plant?.name ?: "No Plant Yet"
    
    val diffMs = if (plant != null) System.currentTimeMillis() - plant.lastWatered else 0L
    val realDaysSince = TimeUnit.MILLISECONDS.toDays(diffMs).toInt()

    // TEMPORARY OVERRIDE FOR DEMONSTRATION
    val daysSince = if (plantName.lowercase() == "test") 6 else realDaysSince

    val lastWateredText = when {
        plant == null -> "No data"
        daysSince == 0 -> "Just now"
        daysSince == 1 -> "Yesterday"
        else -> "$daysSince days ago"
    }

    val intervalDays = try {
        val regex = "\\d+".toRegex()
        val match = regex.find(plant?.wateringInterval ?: "7")
        match?.value?.toInt() ?: 7
    } catch (e: Exception) { 7 }

    val moisturePercent = remember(daysSince, intervalDays, plant) {
        if (plant == null) return@remember 0
        if (plantName.lowercase() == "test") return@remember 15 // Force 15% for visual test

        val fraction = 1f - (daysSince.toFloat() / intervalDays.toFloat())
        (fraction.coerceIn(0f, 1f) * 100).toInt()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
            .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(top = 39.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        // Plant Title and Last Watered
        Text(
            text = plantName,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.WaterDrop,
                contentDescription = null,
                tint = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Last watered: $lastWateredText",
                fontSize = 16.sp,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Care Info Section
        Text(
            text = "Care Info",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeWidget(
                icon = { Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp)) },
                text = plant?.wateringInterval ?: "Weekly"
            )
            HomeWidget(
                icon = { Icon(Icons.Outlined.WbSunny, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp)) },
                text = plant?.sunlightNeeds ?: "Indirect"
            )
            HomeWidget(
                icon = { Icon(Icons.Outlined.Thermostat, contentDescription = null, tint = androidx.compose.material3.MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp)) },
                text = "20-25 °C"
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Moisture Chart
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MoistureChart(percentage = moisturePercent)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Mark as Watered Button
        Button(
            onClick = { 
                if (plant != null && plantViewModel != null) {
                    plantViewModel.markPlantWatered(plant.id)
                }
            },
            enabled = plant != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(26.5.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mark as Watered",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeCardPreview() {
    PlantCareTheme {
        HomeCard(plant = null, plantViewModel = null)
    }
}
