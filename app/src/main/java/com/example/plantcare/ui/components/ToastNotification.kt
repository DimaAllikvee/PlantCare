package com.example.plantcare.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay

enum class ToastType { SUCCESS, ERROR }

data class ToastMessage(
    val message: String,
    val type: ToastType
)

/**
 * Renders a toast notification using Popup — appears above ALL windows,
 * including modals, dialogs, and bottom sheets.
 *
 * Position: bottom-center, ~90dp above the bottom navigation bar.
 * Auto-dismisses after 3 seconds.
 */
@Composable
fun ToastNotification(
    toast: ToastMessage?,
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    // Animate in immediately when toast appears; auto-dismiss after 3s
    LaunchedEffect(toast) {
        if (toast != null) {
            isVisible = true
            delay(3000)
            isVisible = false
            delay(350) // wait for exit animation before clearing state
            onDismiss()
        } else {
            isVisible = false
        }
    }

    // Only create the Popup window when there is an active toast
    if (toast != null) {
        Popup(
            alignment = Alignment.BottomCenter,
            properties = PopupProperties(
                focusable = false,           // don't steal focus from dialogs
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            // Full-width container with bottom padding to sit above the bottom nav
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 90.dp), // above 80dp bottom nav + margin
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 350)
                    ) + fadeIn(animationSpec = tween(250)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeOut(animationSpec = tween(200))
                ) {
                    when (toast.type) {
                        ToastType.SUCCESS -> SuccessToast(
                            message = toast.message,
                            onDismiss = { isVisible = false }
                        )
                        ToastType.ERROR -> ErrorToast(
                            message = toast.message,
                            onDismiss = { isVisible = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessToast(message: String, onDismiss: () -> Unit) {
    val successBg = Color(0xFF0F5238)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = successBg.copy(alpha = 0.3f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(successBg)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Checkmark icon circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF1A7A52), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun ErrorToast(message: String, onDismiss: () -> Unit) {
    val errorBg = Color(0xFF191C1B)
    val errorIconColor = Color(0xFFFF8A80)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(errorBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Error icon box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFBA1A1A).copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                tint = errorIconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
