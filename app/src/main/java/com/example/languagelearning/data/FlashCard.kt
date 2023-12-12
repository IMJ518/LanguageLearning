package com.example.languagelearning.data

data class FlashCard(
    val name: String,
    val category: String,
    val image: String,
    val favorite: Boolean = false)