package com.example.plantcare.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.plantcare.ui.navigation.PlantCareBottomNavigation
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray
import com.example.plantcare.ui.settings.ChangeEmailModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPlants: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F9FB)
    val textDarkGreen = Color(0xFF0F5238)
    val surfaceLightGreen = Color(0xFFDDE4DC)
    val PrimaryGreen = Color(0xFF2D6A4F)
    val textGray = Color(0xFF404943)
    
    var showAccountSettingsModal by remember { mutableStateOf(false) }
    var showChangePasswordModal by remember { mutableStateOf(false) }
    var showChangeEmailModal by remember { mutableStateOf(false) }
    var showAppearanceModal by remember { mutableStateOf(false) }

    if (showAccountSettingsModal) {
        AccountSettingsModal(
            onDismiss = { showAccountSettingsModal = false },
            onChangePasswordClick = { showChangePasswordModal = true },
            onChangeEmailClick = { showChangeEmailModal = true }
        )
    }

    if (showChangePasswordModal) {
        ChangePasswordModal(onDismiss = { showChangePasswordModal = false })
    }

    if (showChangeEmailModal) {
        ChangeEmailModal(
            onDismiss = { showChangeEmailModal = false },
            onSaveEmail = { newEmail, confirmEmail ->
                showChangeEmailModal = false
            }
        )
    }

    if (showAppearanceModal) {
        AppearanceBottomSheet(onDismiss = { showAppearanceModal = false })
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Profile",
                        color = textDarkGreen,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                        textAlign = TextAlign.Center
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = textDarkGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            PlantCareBottomNavigation(
                currentRoute = "profile",
                onNavigateToHome = onNavigateToHome,
                onNavigateToCalendar = onNavigateToCalendar,
                onNavigateToPlants = onNavigateToPlants,
                onNavigateToProfile = { /* Already here */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Avatar container
            Box(
                modifier = Modifier.size(128.dp),
                contentAlignment = Alignment.Center
            ) {
                // The actual avatar circle with clip
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for Avatar Image
                    Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.LightGray))
                }
                
                // Edit Button overlapping
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(32.dp)
                        .background(PrimaryGreen, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { /* Edit Action */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "Alex Carter", color = textDarkGreen, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = "alex.carter@botanical.com", color = textGray, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(modifier = Modifier.weight(1f), count = "12", label = "PLANTS", backgroundColor = surfaceLightGreen, contentColor = textDarkGreen)
                StatCard(modifier = Modifier.weight(1f), count = "7", label = "DUE TODAY", backgroundColor = surfaceLightGreen, contentColor = textDarkGreen)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Settings Menu
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SettingsItem(title = "Account Settings", icon = Icons.Outlined.Person, onClick = { showAccountSettingsModal = true })
                var notificationsEnabled by remember { mutableStateOf(true) }
                SettingsItemWithToggle(
                    title = "Notifications",
                    icon = Icons.Outlined.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
                SettingsItem(title = "Appearance", icon = Icons.Outlined.DarkMode, onClick = { showAppearanceModal = true })
                SettingsItem(title = "Help & Support", icon = Icons.Outlined.HelpOutline)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsItem(
                    title = "Log Out",
                    icon = Icons.Outlined.ExitToApp,
                    contentColor = Color(0xFFBA1A1A),
                    iconBackgroundColor = Color(0xFFFFDAD6).copy(alpha = 0.3f),
                    hasArrow = false,
                    onClick = onLogout
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, count: String, label: String, backgroundColor: Color, contentColor: Color) {
    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = count, color = contentColor, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = contentColor.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.6.sp)
    }
}

@Composable
fun SettingsItem(
    title: String,
    icon: ImageVector,
    contentColor: Color = Color(0xFF0F5238),
    iconBackgroundColor: Color = Color(0xFFDDE4DC).copy(alpha = 0.5f),
    hasArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBackgroundColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = contentColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        
        if (hasArrow) {
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SettingsItemWithToggle(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFDDE4DC).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF0F5238), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = Color(0xFF0F5238), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF2D6A4F)
            )
        )
    }
}



@Composable
fun AccountSettingsModal(
    onDismiss: () -> Unit, 
    onChangePasswordClick: () -> Unit,
    onChangeEmailClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // to allow custom width
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of screen width
                .wrapContentHeight()
                .heightIn(max = 700.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF8FAF8))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Account Settings",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D6A4F)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF2D6A4F))
                    }
                }
                
                // Content
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp)
                ) {
                    // Personal Information
                    SectionTitle("PERSONAL INFORMATION")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(label = "FULL NAME", value = "Alex Carter", onClick = {})
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(label = "EMAIL", value = "j.green@botanica.com", onClick = onChangeEmailClick)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Security
                    SectionTitle("SECURITY")
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionRow(icon = Icons.Outlined.Lock, title = "Change Password", onClick = onChangePasswordClick)
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionRowWithSubtitle(icon = Icons.Outlined.Security, title = "Two-Factor\nAuthentication", subtitle = "Enabled", onClick = {})
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Account Preferences
                    SectionTitle("ACCOUNT PREFERENCES")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(label = "LANGUAGE", value = "English (US)")
                    
                    Spacer(modifier = Modifier.height(44.dp))
                }
                
                // Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F))
                    ) {
                        Text(text = "Save Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F5238).copy(alpha = 0.6f),
        letterSpacing = 1.2.sp
    )
}

@Composable
fun InfoRow(label: String, value: String, onClick: (() -> Unit)? = null) {
    val modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFFE9F0E7))
        .let { if (onClick != null) it.clickable(onClick = onClick) else it }
        .padding(16.dp)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F5238).copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D6A4F))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun ActionRow(icon: ImageVector, title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE9F0E7))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF2D6A4F), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D6A4F))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun ActionRowWithSubtitle(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE9F0E7))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF2D6A4F), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2D6A4F), lineHeight = 20.sp)
                Text(text = subtitle, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color(0xFF047857).copy(alpha = 0.7f))
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordModal(onDismiss: () -> Unit) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight() // Fixes empty space bug
                .heightIn(max = 700.dp) // Limits max height on small screens
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF8FAF8))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Change Password",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F5238)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .background(Color(0xFFE9F0E7), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF0F5238), modifier = Modifier.size(18.dp))
                    }
                }

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false) // Fixes empty space bug while still allowing scrolling if needed
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp)
                ) {
                    // Current Password
                    SectionTitle("CURRENT PASSWORD")
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        placeholder = "Enter current password",
                        isVisible = currentPasswordVisible,
                        onVisibilityChange = { currentPasswordVisible = it }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // New Password
                    SectionTitle("NEW PASSWORD")
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = "Min. 8 characters",
                        isVisible = newPasswordVisible,
                        onVisibilityChange = { newPasswordVisible = it }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    // Confirm Password
                    SectionTitle("CONFIRM NEW PASSWORD")
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Repeat new password",
                        isVisible = confirmPasswordVisible,
                        onVisibilityChange = { confirmPasswordVisible = it }
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Security Requirements Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE9F0E7), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF2D6A4F),
                            modifier = Modifier.size(20.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Security Requirements", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F5238))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your password must be at least 8 characters long and include a mix of letters and numbers for better atelier security.",
                                fontSize = 12.sp,
                                color = Color(0xFF404943),
                                lineHeight = 18.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F))
                    ) {
                        Text(text = "Update Password", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isVisible: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color(0xFF9E9E9E), fontSize = 14.sp) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F4F1),
            focusedContainerColor = Color(0xFFF1F4F1),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (isVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange(!isVisible) }) {
                Icon(
                    imageVector = if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (isVisible) "Hide password" else "Show password",
                    tint = Color(0xFF625B71)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTheme by remember { mutableStateOf("Light") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFF8F9FB),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFE1E2E4), width = 48.dp) },
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Appearance",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D6A4F)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black) // Temporary close icon, Figma uses SVG
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "THEME SELECTION",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF596059),
                letterSpacing = 0.7.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Light Theme Selection
            ThemeCard(
                title = "Light",
                subtitle = "Soft mist and airy whites",
                icon = Icons.Outlined.WbSunny,
                isSelected = selectedTheme == "Light",
                onClick = { selectedTheme = "Light" }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Dark Theme Selection
            ThemeCard(
                title = "Dark",
                subtitle = "Deep forest shadows",
                icon = Icons.Outlined.DarkMode,
                isSelected = selectedTheme == "Dark",
                onClick = { selectedTheme = "Dark" }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // System Theme Selection
            ThemeCard(
                title = "System",
                subtitle = "Adapts to device settings",
                icon = Icons.Outlined.Settings,
                isSelected = selectedTheme == "System",
                onClick = { selectedTheme = "System" }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Apply Changes Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D6A4F)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(text = "Apply Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Footer text
            Text(
                text = "Changes will take effect immediately",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF404943),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ThemeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF0F5238) else Color.Transparent
    val backgroundColor = if (isSelected) Color.White else Color(0xFFEDEEF0)
    val computedIconBgColor = if (isSelected) Color(0xFFB1F0CE) else Color(0xFFE1E2E4)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(22.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(computedIconBgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Color(0xFF191C1E), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1E))
                Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF596059))
            }
        }
        
        // Selection Indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF0F5238), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(16.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(2.dp, Color(0xFFBFC9C1), CircleShape)
            )
        }
    }
}
