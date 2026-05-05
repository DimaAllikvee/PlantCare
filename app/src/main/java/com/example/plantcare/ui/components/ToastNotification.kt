package com.example.plantcare.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class ToastType { SUCCESS, ERROR }

data class ToastMessage(
    val message: String,
    val type: ToastType
)

@Composable
fun ToastNotification(
    toast: ToastMessage?,
    onDismiss: () -> Unit
) {
    // Auto-dismiss after 3 seconds
    LaunchedEffect(toast) {
        if (toast != null) {
            delay(3000)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 96.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            if (toast != null) {
                when (toast.type) {
                    ToastType.SUCCESS -> SuccessToast(message = toast.message, onDismiss = onDismiss)
                    ToastType.ERROR -> ErrorToast(message = toast.message, onDismiss = onDismiss)
                }
            }
        }
    }
}

@Composable
private fun SuccessToast(message: String, onDismiss: () -> Unit) {
    // Success: dark green background #0F5238, rounded icon with checkmark
    val successBg = Color(0x0F, 0x52, 0x38)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = successBg.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(successBg)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Rounded icon bubble
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color(0x2D, 0x6A, 0x4F), // slightly lighter green
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        // Text
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        // Close button
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun ErrorToast(message: String, onDismiss: () -> Unit) {
    // Error: near-black background #191C1B, white border 5% opacity, red error icon
    val errorBg = Color(0x19, 0x1C, 0x1B)
    val errorIconColor = Color(0xFF, 0x8A, 0x80) // salmon/red

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(errorBg)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Error icon box - red tinted semi-transparent bg
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xBA, 0x1A, 0x1A, 0x33), // #BA1A1A at 20% alpha
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                tint = errorIconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        // Text
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 17.5.sp,
            modifier = Modifier.weight(1f)
        )

        // Close button
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
