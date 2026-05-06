package com.example.plantcare.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantcare.ui.login.AuthViewModel
import com.example.plantcare.ui.login.AuthState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Plant Illustration Top
        Spacer(modifier = Modifier.height(28.dp))
        Image(
            painter = painterResource(id = R.drawable.plant_illustration),
            contentDescription = "Potted Plants Illustration",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(216.dp)
                .height(221.dp)
        )

        // Title
        Text(
            text = "Create Account",
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        // Form Fields Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Username Icon",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(23.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(23.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .padding(end = 16.dp)
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(23.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm Password", color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Text(
                        text = if (confirmPasswordVisible) "Hide" else "Show",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable { confirmPasswordVisible = !confirmPasswordVisible }
                            .padding(end = 16.dp)
                    )
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(23.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Authentication State Feedback
        when (authState) {
            is AuthState.Loading -> {
                CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.padding(bottom = 16.dp))
            }
            is AuthState.Success -> {
                LaunchedEffect(Unit) {
                    onSignUpSuccess()
                    authViewModel.resetState()
                }
            }
            is AuthState.Error -> {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> {}
        }
        
        // Sign Up Button
        Button(
            onClick = {
                if (password == confirmPassword) {
                    authViewModel.signUp(email, password, username)
                } else {
                }
            },
            shape = RoundedCornerShape(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(85.dp),
            enabled = authState !is AuthState.Loading
        ) {
            Text(
                text = "Sign Up",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Footer Text
        Row(
            modifier = Modifier.padding(bottom = 35.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Login in",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
