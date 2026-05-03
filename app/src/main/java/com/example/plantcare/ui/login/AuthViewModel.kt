package com.example.plantcare.ui.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun signUp(email: String, pass: String, username: String) {
        if (email.isBlank() || pass.isBlank() || username.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update user profile with username can be done here.
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }
    
    fun changePassword(currentPass: String, newPass: String, confirmPass: String) {
        if (newPass != confirmPass) {
            _authState.value = AuthState.Error("New passwords do not match")
            return
        }
        if (currentPass.isBlank() || newPass.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }
        
        val user = auth.currentUser
        if (user == null || user.email == null) {
            _authState.value = AuthState.Error("User not logged in")
            return
        }

        _authState.value = AuthState.Loading
        
        // Re-authenticate first
        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(user.email!!, currentPass)
        user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
            if (reauthTask.isSuccessful) {
                user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        _authState.value = AuthState.Success
                    } else {
                        _authState.value = AuthState.Error(updateTask.exception?.message ?: "Failed to update password")
                    }
                }
            } else {
                _authState.value = AuthState.Error(reauthTask.exception?.message ?: "Re-authentication failed. Incorrect current password.")
            }
        }
    }

    fun changeEmail(newEmail: String, confirmEmail: String) {
        if (newEmail != confirmEmail) {
            _authState.value = AuthState.Error("Emails do not match")
            return
        }
        if (newEmail.isBlank()) {
            _authState.value = AuthState.Error("Email cannot be empty")
            return
        }

        val user = auth.currentUser
        if (user == null) {
            _authState.value = AuthState.Error("User not logged in")
            return
        }

        _authState.value = AuthState.Loading
        
        // Firebase highly recommends verifying the new email before updating
        user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Success
            } else {
                // If it fails due to recent login required, we show the error
                _authState.value = AuthState.Error(task.exception?.message ?: "Failed to send verification email")
            }
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
