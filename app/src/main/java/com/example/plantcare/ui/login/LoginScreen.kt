package com.example.plantcare.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plantcare.R
import com.example.plantcare.ui.theme.PrimaryGreen
import com.example.plantcare.ui.theme.SurfaceLightGreen
import com.example.plantcare.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Banner Image
        Image(
            painter = painterResource(id = R.drawable.leaf_pattern_bg),
            contentDescription = "Botanical Header",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(235.dp)
                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 43.dp, bottomStart = 43.dp, bottomEnd = 0.dp))
        )

        Spacer(modifier = Modifier.height(49.dp))

        // Title
        Text(
            text = "Welcome Back",
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryGreen,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email", color = TextGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = TextGray
                )
            },
            shape = RoundedCornerShape(23.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceLightGreen,
                unfocusedContainerColor = SurfaceLightGreen,
                focusedBorderColor = TextGray,
                unfocusedBorderColor = TextGray,
                cursorColor = PrimaryGreen
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(70.dp)
        )

        Spacer(modifier = Modifier.height(13.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter your password", color = TextGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    tint = TextGray
                )
            },
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    color = TextGray,
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(end = 16.dp)
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(23.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceLightGreen,
                unfocusedContainerColor = SurfaceLightGreen,
                focusedBorderColor = TextGray,
                unfocusedBorderColor = TextGray,
                cursorColor = PrimaryGreen
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(70.dp)
        )

        // Forgot Password
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle click */ }
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
                    onLoginSuccess()
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

        // Login Button
        Button(
            onClick = { authViewModel.login(email, password) },
            shape = RoundedCornerShape(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(85.dp),
            enabled = authState !is AuthState.Loading
        ) {
            Text(
                text = "Login",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(49.dp))

        // Sign Up
        Row(
            modifier = Modifier
                .padding(bottom = 35.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = TextGray
            )
            Text(
                text = "Sign Up",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen,
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
