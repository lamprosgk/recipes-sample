package com.lamprosgk.data

import com.lamprosgk.data.local.RecipeEntity
import com.lamprosgk.domain.model.Recipe
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val count: Int,
    val results: List<RecipeData>
)

@Serializable
data class RecipeData(
    val id: Int,
    val name: String,
    val description: String,
    val instructions: List<InstructionData>,
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int,
    @SerialName("total_time_minutes") val totalTimeMinutes: Int,
    @SerialName("num_servings") val numServings: Int,
    val nutrition: NutritionData? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    val tags: List<TagData>
)

@Serializable
data class InstructionData(
    @SerialName("display_text") val displayText: String,
    val position: Int
)

@Serializable
data class NutritionData(
    val calories: Int
)

@Serializable
data class TagData(
    @SerialName("display_name") val displayName: String,
    val type: String
)

fun RecipeData.asDomainModel() = Recipe(
    id = id,
    name = name,
    description = description,
    instructionsSteps = instructions.sortedBy { it.position }
        .map { it.displayText },
    prepTimeMinutes = prepTimeMinutes,
    totalTimeMinutes = totalTimeMinutes,
    numServings = numServings,
    calories = nutrition?.calories ?: 0,
    thumbnailUrl = thumbnailUrl,
    tags = tags.map { it.displayName }
)

fun RecipeData.asEntityModel() = RecipeEntity(
    id = id,
    name = name,
    description = description,
    instructionsSteps = instructions.sortedBy { it.position }.joinToString { it.displayText },
    prepTimeMinutes = prepTimeMinutes,
    totalTimeMinutes = totalTimeMinutes,
    numServings = numServings,
    calories = nutrition?.calories ?: 0,
    thumbnailUrl = thumbnailUrl,
    tags = tags.joinToString { it.displayName },
    isFavourite = false
)