package com.example.plantcare.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PlantCareTheme
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.TextGray
import com.example.plantcare.ui.theme.WidgetBackground
import com.example.plantcare.ui.theme.WidgetText

@Composable
fun HomeWidget(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(WidgetBackground, RoundedCornerShape(100.dp))
            .height(32.dp)
            .padding(horizontal = 12.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = WidgetText,
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(170.dp)
    ) {
        Canvas(modifier = Modifier.size(140.dp)) { // Slightly smaller canvas to fit stroke
            // Draw background track
            drawArc(
                color = Color(0xFFCDE0D5), // Matches the very light green from Figma
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
            
            // Draw progress arc
            drawArc(
                color = PrimaryGreen,
                startAngle = 135f,
                sweepAngle = 270f * (percentage / 100f),
                useCenter = false,
                style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$percentage%",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = WidgetText
            )
            Text(
                text = "Moisture Level",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = WidgetText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                imageVector = Icons.Outlined.WaterDrop,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun HomeCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
            .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
            .background(CardBackground)
            .padding(top = 39.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
    ) {
        // Plant Title and Last Watered
        Text(
            text = "Ficus Elastica",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.WaterDrop,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Last watered: 2 days ago",
                fontSize = 16.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Care Info Section
        Text(
            text = "Care Info",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = WidgetText,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeWidget(
                icon = { 
                    Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(14.dp)) 
                },
                text = "Weekly"
            )
            HomeWidget(
                icon = { 
                    Icon(Icons.Outlined.WbSunny, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(14.dp)) 
                },
                text = "Indirect Light"
            )
            HomeWidget(
                icon = { 
                    Icon(Icons.Outlined.Thermostat, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(14.dp)) 
                },
                text = "20-25 °C"
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Moisture Chart
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            MoistureChart(percentage = 65)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Mark as Watered Button
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
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
        HomeCard()
    }
}
