package com.example.plantcare.ui.newplant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantcare.data.api.RetrofitInstance
import com.example.plantcare.data.api.SpeciesDetailResponse
import com.example.plantcare.data.api.SpeciesListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlantCatalogViewModel : ViewModel() {

    private val _searchResults = MutableStateFlow<List<SpeciesListItem>>(emptyList())
    val searchResults: StateFlow<List<SpeciesListItem>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _selectedPlantDetails = MutableStateFlow<SpeciesDetailResponse?>(null)
    val selectedPlantDetails: StateFlow<SpeciesDetailResponse?> = _selectedPlantDetails.asStateFlow()

    private val _isLoadingDetails = MutableStateFlow(false)
    val isLoadingDetails: StateFlow<Boolean> = _isLoadingDetails.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var searchJob: Job? = null

    fun searchSpecies(query: String) {
        searchJob?.cancel()
        if (query.length < 2) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // Debounce — wait 400ms before sending request
            _isSearching.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.searchPlants(query = query, indoor = 1)
                _searchResults.value = response.data.take(10) // Limit to 10 results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                _errorMessage.value = "Search error: ${e.message}"
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun getPlantDetails(id: Int) {
        viewModelScope.launch {
            _isLoadingDetails.value = true
            _errorMessage.value = null
            try {
                val details = RetrofitInstance.api.getPlantDetails(id = id)
                _selectedPlantDetails.value = details
            } catch (e: Exception) {
                _selectedPlantDetails.value = null
                _errorMessage.value = "Details error: ${e.message}"
            } finally {
                _isLoadingDetails.value = false
            }
        }
    }

    fun clearSearch() {
        _searchResults.value = emptyList()
    }

    fun clearSelectedDetails() {
        _selectedPlantDetails.value = null
    }

    /**
     * Maps the API's watering string (e.g., "Frequent", "Average", "Minimum")
     * to the app's interval format (e.g., "Every 3 days").
     */
    fun mapWateringToInterval(watering: String?, benchmark: String?): String {
        // Try to use the benchmark first (e.g. "5-7 days")
        if (!benchmark.isNullOrBlank()) {
            val daysMatch = Regex("(\\d+)").find(benchmark)
            if (daysMatch != null) {
                val days = daysMatch.value.toIntOrNull()
                if (days != null) {
                    return "Every $days days"
                }
            }
        }
        // Fallback to the watering category
        return when (watering?.lowercase()) {
            "frequent" -> "Every 2 days"
            "average" -> "Every 5 days"
            "minimum" -> "Every 14 days"
            "none" -> "Every 14 days"
            else -> "Every 7 days"
        }
    }

    /**
     * Maps the API's sunlight list (e.g., ["full sun", "part shade"])
     * to the app's "Low", "Medium", "High" format.
     */
    fun mapSunlight(sunlight: List<String>?): String {
        if (sunlight.isNullOrEmpty()) return "Medium"
        val primary = sunlight.first().lowercase()
        return when {
            "full sun" in primary || "full" in primary -> "High"
            "part shade" in primary || "partial" in primary || "filtered" in primary -> "Medium"
            "shade" in primary || "low" in primary -> "Low"
            else -> "Medium"
        }
    }
}
