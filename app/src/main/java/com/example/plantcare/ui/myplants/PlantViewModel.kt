package com.example.plantcare.ui.myplants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.example.plantcare.data.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.UUID

sealed class PlantState {
    object Idle : PlantState()
    object Loading : PlantState()
    object Success : PlantState()
    data class Error(val message: String) : PlantState()
}

class PlantViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    private val _plantState = MutableStateFlow<PlantState>(PlantState.Idle)
    val plantState: StateFlow<PlantState> = _plantState.asStateFlow()

    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    private val _toastMessage = MutableStateFlow<com.example.plantcare.ui.components.ToastMessage?>(null)
    val toastMessage: StateFlow<com.example.plantcare.ui.components.ToastMessage?> = _toastMessage.asStateFlow()

    fun showToast(message: String, type: com.example.plantcare.ui.components.ToastType = com.example.plantcare.ui.components.ToastType.SUCCESS) {
        _toastMessage.value = com.example.plantcare.ui.components.ToastMessage(message, type)
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    init {
        fetchPlants()
    }

    private val storage = FirebaseStorage.getInstance()

    fun addPlant(
        name: String, 
        species: String, 
        interval: String, 
        mistingInterval: String, 
        fertilizingInterval: String, 
        sunlight: String, 
        imageUrl: String
    ) {
        val user = auth.currentUser
        if (user == null) {
            _plantState.value = PlantState.Error("User not logged in")
            return
        }
        
        if (name.isBlank() || species.isBlank() || interval.isBlank()) {
            _plantState.value = PlantState.Error("Please fill in all details")
            return
        }

        _plantState.value = PlantState.Loading
        val plantId = UUID.randomUUID().toString()

        // Upload image to Firebase Storage for permanent URL
        viewModelScope.launch {
            val permanentUrl = if (imageUrl.isNotEmpty()) {
                uploadImageToStorage(imageUrl, plantId, user.uid)
            } else ""

            val newPlant = Plant(
                id = plantId,
                name = name,
                species = species,
                wateringInterval = interval,
                mistingInterval = mistingInterval,
                fertilizingInterval = fertilizingInterval,
                sunlightNeeds = sunlight,
                imageUrl = permanentUrl,
                userId = user.uid
            )

            try {
                db.collection("plants").document(plantId).set(newPlant).await()
                _plantState.value = PlantState.Success
                showToast("Plant added successfully")
                fetchPlants()
            } catch (e: Exception) {
                _plantState.value = PlantState.Error(e.message ?: "Error adding plant")
                showToast(e.message ?: "Error adding plant", com.example.plantcare.ui.components.ToastType.ERROR)
            }
        }
    }

    /**
     * Downloads an image from a URL and uploads it to Firebase Storage.
     * Returns the permanent download URL, or the original URL if upload fails.
     */
    private suspend fun uploadImageToStorage(
        sourceUrl: String, 
        plantId: String, 
        userId: String
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                // Download image bytes from API URL
                val imageBytes = URL(sourceUrl).readBytes()
                
                // Upload to Firebase Storage
                val storageRef = storage.reference
                    .child("plant_images/$userId/$plantId.jpg")
                storageRef.putBytes(imageBytes).await()
                
                // Get permanent download URL
                storageRef.downloadUrl.await().toString()
            } catch (e: Exception) {
                // If upload fails, fall back to original URL
                sourceUrl
            }
        }
    }

    fun updatePlant(
        plantId: String,
        name: String, 
        species: String, 
        interval: String, 
        mistingInterval: String, 
        fertilizingInterval: String, 
        sunlight: String, 
        imageUrl: String
    ) {
        val user = auth.currentUser
        if (user == null) {
            _plantState.value = PlantState.Error("User not logged in")
            return
        }
        
        if (name.isBlank() || species.isBlank() || interval.isBlank()) {
            _plantState.value = PlantState.Error("Please fill in all details")
            return
        }

        _plantState.value = PlantState.Loading
        
        val updates = mapOf(
            "name" to name,
            "species" to species,
            "wateringInterval" to interval,
            "mistingInterval" to mistingInterval,
            "fertilizingInterval" to fertilizingInterval,
            "sunlightNeeds" to sunlight,
            "imageUrl" to imageUrl
        )

        db.collection("plants").document(plantId)
            .update(updates)
            .addOnSuccessListener {
                _plantState.value = PlantState.Success
                fetchPlants() // refresh the list
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error updating plant")
            }
    }

    fun deletePlant(plantId: String) {
        _plantState.value = PlantState.Loading
        db.collection("plants").document(plantId)
            .delete()
            .addOnSuccessListener {
                _plantState.value = PlantState.Success
                showToast("Plant deleted")
                fetchPlants() // refresh the list
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error deleting plant")
                showToast(e.message ?: "Error deleting plant", com.example.plantcare.ui.components.ToastType.ERROR)
            }
    }

    fun markPlantWatered(plantId: String) {
        val updates = mapOf(
            "lastWatered" to System.currentTimeMillis(),
            "nextWateringOverride" to null
        )
        db.collection("plants").document(plantId)
            .update(updates)
            .addOnSuccessListener {
                showToast("Watering updated")
                fetchPlants() // refresh the list to reflect updated moisture
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error updating plant")
                showToast(e.message ?: "Error updating plant", com.example.plantcare.ui.components.ToastType.ERROR)
            }
    }

    fun markPlantMisted(plantId: String) {
        val updates = mapOf(
            "lastMisted" to System.currentTimeMillis(),
            "nextMistingOverride" to null
        )
        db.collection("plants").document(plantId)
            .update(updates)
            .addOnSuccessListener {
                fetchPlants()
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error updating plant")
            }
    }

    fun markPlantFertilized(plantId: String) {
        val updates = mapOf(
            "lastFertilized" to System.currentTimeMillis(),
            "nextFertilizingOverride" to null
        )
        db.collection("plants").document(plantId)
            .update(updates)
            .addOnSuccessListener {
                fetchPlants()
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error updating plant")
            }
    }
    
    fun rescheduleTask(plantId: String, taskType: String, newDateMillis: Long) {
        val fieldToUpdate = when (taskType) {
            "Watering" -> "nextWateringOverride"
            "Misting" -> "nextMistingOverride"
            "Fertilizing" -> "nextFertilizingOverride"
            else -> return
        }
        
        db.collection("plants").document(plantId)
            .update(fieldToUpdate, newDateMillis)
            .addOnSuccessListener {
                fetchPlants()
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error rescheduling task")
            }
    }

    fun fetchPlants() {
        val user = auth.currentUser ?: return
        
        db.collection("plants")
            .whereEqualTo("userId", user.uid)
            .get()
            .addOnSuccessListener { result ->
                val plantList = result.documents.mapNotNull { it.toObject(Plant::class.java) }
                _plants.value = plantList
            }
            .addOnFailureListener { e ->
            }
    }

    fun resetState() {
        _plantState.value = PlantState.Idle
    }
}
