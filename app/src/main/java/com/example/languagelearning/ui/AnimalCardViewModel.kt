package com.example.languagelearning.ui

import androidx.lifecycle.ViewModel
import com.example.languagelearning.data.AnimalCardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AnimalCardViewModel : ViewModel() {
    private val _AnimalUiState = MutableStateFlow(AnimalCardState())
    val AnimalUiState: StateFlow<AnimalCardState> = _AnimalUiState.asStateFlow()

    fun setAnimalFlashCard(category: String, names: List<String>, images: List<String>) {
        _AnimalUiState.update { currentState ->
            currentState.copy(
                animalNames = names,
                animalPhotos = images
            )
        }
    }
}