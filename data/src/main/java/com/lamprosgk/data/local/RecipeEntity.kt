package com.lamprosgk.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lamprosgk.domain.model.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val instructionsSteps: String,
    val ingredients: String,
    val prepTimeMinutes: Int,
    val totalTimeMinutes: Int,
    val numServings: Int,
    val calories: Int,
    val thumbnailUrl: String,
    val tags: String,
    val isFavourite: Boolean
)

fun RecipeEntity.asDomainModel() = Recipe(
    id = id,
    name = name,
    description = description,
    ingredients = ingredients.split("||"),
    instructionsSteps = instructionsSteps.split("||"),
    prepTimeMinutes = prepTimeMinutes,
    totalTimeMinutes = totalTimeMinutes,
    numServings = numServings,
    calories = calories,
    thumbnailUrl = thumbnailUrl,
    tags = tags.split(","),
    isFavourite = isFavourite
)
