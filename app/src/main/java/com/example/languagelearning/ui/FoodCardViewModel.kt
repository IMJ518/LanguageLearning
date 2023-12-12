package com.example.languagelearning.ui

import androidx.lifecycle.ViewModel
import com.example.languagelearning.data.AnimalCardState
import com.example.languagelearning.data.FoodCardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FoodCardViewModel : ViewModel() {
    private val _FoodUiState = MutableStateFlow(FoodCardState())
    val FoodUiState: StateFlow<FoodCardState> = _FoodUiState.asStateFlow()

    fun setFoodFlashCard(category: String, names: List<String>, images: List<String>) {
        _FoodUiState.update { currentState ->
            currentState.copy(
                foodNames = names,
                foodPhotos = images
            )
        }
    }
}