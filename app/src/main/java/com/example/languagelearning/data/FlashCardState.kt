package com.example.languagelearning.data

data class FlashCardState (
    val animalNames: MutableList<String> = mutableListOf(),
    val animalPhotos: MutableList<String> = mutableListOf(),

    val foodNames: MutableList<String> = mutableListOf(),
    val foodPhotos: MutableList<String> = mutableListOf(),

    val favoriteNames: MutableList<String> = mutableListOf(),
    val favoritePhotos: MutableList<String> = mutableListOf()
)