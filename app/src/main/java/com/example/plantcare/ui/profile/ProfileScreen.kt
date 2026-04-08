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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPlants: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val backgroundColor = Color(0xFFF8F9FB)
    val textDarkGreen = Color(0xFF0F5238)
    val surfaceLightGreen = Color(0xFFDDE4DC)
    val PrimaryGreen = Color(0xFF2D6A4F)
    val textGray = Color(0xFF404943)
    
    var showAccountSettingsModal by remember { mutableStateOf(false) }

    if (showAccountSettingsModal) {
        AccountSettingsModal(onDismiss = { showAccountSettingsModal = false })
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
            ProfileBottomNavigationBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToPlants = onNavigateToPlants
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
                SettingsItem(title = "Appearance", icon = Icons.Outlined.DarkMode)
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
fun ProfileBottomNavigationBar(
    onNavigateToHome: () -> Unit,
    onNavigateToPlants: () -> Unit
) {
    val items = listOf("Home", "Calendar", "Plants", "Profile")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.DateRange, Icons.Filled.LocalFlorist, Icons.Filled.Person)
    val PrimaryGreen = Color(0xFF2D6A4F)
    val SurfaceLightGreen = Color(0xFFDDE4DC)
    val TextGray = Color(0xFF8B9E9B)

    NavigationBar(
        containerColor = SurfaceLightGreen,
        contentColor = PrimaryGreen
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item, fontWeight = FontWeight.Medium) },
                selected = index == 3, // Profile is the 4th item
                onClick = { 
                    if (index == 0) onNavigateToHome()
                    if (index == 2) onNavigateToPlants()
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = PrimaryGreen.copy(alpha = 0.2f),
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray
                )
            )
        }
    }
}

@Composable
fun AccountSettingsModal(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // to allow custom width
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of screen width
                .fillMaxHeight(0.85f)
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
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp)
                ) {
                    // Personal Information
                    SectionTitle("PERSONAL INFORMATION")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(label = "FULL NAME", value = "Alex Carter")
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(label = "EMAIL", value = "j.green@botanica.com")
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Security
                    SectionTitle("SECURITY")
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionRow(icon = Icons.Outlined.Lock, title = "Change Password")
                    Spacer(modifier = Modifier.height(12.dp))
                    ActionRowWithSubtitle(icon = Icons.Outlined.Security, title = "Two-Factor\nAuthentication", subtitle = "Enabled")
                    
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
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE9F0E7))
            .padding(16.dp),
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
fun ActionRow(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE9F0E7))
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
fun ActionRowWithSubtitle(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE9F0E7))
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
