package com.example.plantcare.ui.myplants

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.plantcare.data.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        fetchPlants()
    }

    fun addPlant(name: String, species: String, interval: String, sunlight: String, imageUrl: String) {
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
        val newPlant = Plant(
            id = plantId,
            name = name,
            species = species,
            wateringInterval = interval,
            sunlightNeeds = sunlight,
            imageUrl = imageUrl,
            userId = user.uid
        )

        db.collection("plants").document(plantId)
            .set(newPlant)
            .addOnSuccessListener {
                _plantState.value = PlantState.Success
                fetchPlants() // refresh the list
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error adding plant")
            }
    }

    fun deletePlant(plantId: String) {
        _plantState.value = PlantState.Loading
        db.collection("plants").document(plantId)
            .delete()
            .addOnSuccessListener {
                _plantState.value = PlantState.Success
                fetchPlants() // refresh the list
            }
            .addOnFailureListener { e ->
                _plantState.value = PlantState.Error(e.message ?: "Error deleting plant")
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
                // handle error if needed
            }
    }

    fun resetState() {
        _plantState.value = PlantState.Idle
    }
}
