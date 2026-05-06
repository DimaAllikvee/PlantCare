package com.example.plantcare.ui.settings

import com.example.plantcare.ui.login.AuthState

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.ui.theme.CardBackground
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailModal(
    onDismiss: () -> Unit,
    onSaveEmail: (String, String) -> Unit,
    currentEmail: String,
    authState: AuthState
) {
    var newEmail by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .windowInsetsPadding(WindowInsets.ime),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change Email",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = PrimaryGreen)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Current Email (Read-Only)
            SettingsInputField(
                label = "Current Email",
                value = currentEmail,
                onValueChange = {},
                readOnly = true,
                placeholder = ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // New Email
            SettingsInputField(
                label = "New Email",
                value = newEmail,
                onValueChange = { newEmail = it },
                placeholder = "Enter new email address"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm New Email
            SettingsInputField(
                label = "Confirm New Email",
                value = confirmEmail,
                onValueChange = { confirmEmail = it },
                placeholder = "Confirm new email address"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Helper text
            Text(
                text = "A verification code will be sent to your new email address to confirm the change.",
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier.fillMaxWidth()
            )

            if (authState is AuthState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Button
            Button(
                onClick = { onSaveEmail(newEmail, confirmEmail) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(16.dp),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Update Email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = PrimaryGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = { Text(placeholder, color = TextGray) },
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = CardBackground,
                focusedContainerColor = CardBackground,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryGreen,
                disabledContainerColor = Color(0xFFF1F2F4),
                disabledBorderColor = Color.Transparent,
                disabledTextColor = TextGray
            ),
            enabled = !readOnly,
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}
