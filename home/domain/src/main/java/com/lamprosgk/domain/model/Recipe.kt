package com.lamprosgk.domain.model

data class Recipe(
    val name: String,
    val description: String,
    val instructionsSteps: List<String>,
    val prepTimeMinutes: Int,
    val totalTimeMinutes: Int,
    val numServings: Int,
    val calories: Int,
    val thumbnailUrl: String,
    val tags: List<String>
)
