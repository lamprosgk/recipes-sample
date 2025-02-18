package com.lamprosgk.data.remote

import com.lamprosgk.data.local.RecipeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val count: Int,
    val results: List<RecipeData> = emptyList()
)

@Serializable
data class RecipeData(
    val id: Int,
    val name: String,
    val description: String,
    val instructions: List<InstructionData> = emptyList(),
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int,
    @SerialName("total_time_minutes") val totalTimeMinutes: Int,
    @SerialName("num_servings") val numServings: Int,
    val nutrition: NutritionData? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    val tags: List<TagData> = emptyList(),
    val sections: List<SectionData> = emptyList()
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

@Serializable
data class SectionData(
    val components: List<ComponentData> = emptyList()
)

@Serializable
data class ComponentData(
    @SerialName("raw_text") val rawText: String
)

fun RecipeData.asEntityModel() = RecipeEntity(
    id = id,
    name = name,
    description = description,
    instructionsSteps = instructions.sortedBy { it.position }.joinToString(separator = "||") { it.displayText },
    ingredients = sections.flatMap { it.components }.joinToString(separator = "||") { it.rawText },
    prepTimeMinutes = prepTimeMinutes,
    totalTimeMinutes = totalTimeMinutes,
    numServings = numServings,
    calories = nutrition?.calories ?: 0,
    thumbnailUrl = thumbnailUrl,
    tags = tags.joinToString { it.displayName },
    isFavourite = false
)