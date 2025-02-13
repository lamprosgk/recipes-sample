package com.lamprosgk.data

import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RecipesRemoteDataSource
): RecipesRepository {

    val sampleRecipeData = RecipeData(
        name = "Simple Avocado Toast",
        description = "Quick and easy avocado toast with a pinch of salt",
        instructions = listOf(
            InstructionData(
                displayText = "Toast the bread until golden",
                position = 1
            ),
            InstructionData(
                displayText = "Mash avocado and spread on toast",
                position = 2
            ),
            InstructionData(
                displayText = "Sprinkle with salt and serve",
                position = 3
            )
        ),
        prepTimeMinutes = 5,
        totalTimeMinutes = 10,
        numServings = 1,
        nutrition = NutritionData(
            calories = 220
        ),
        thumbnailUrl = "https://example.com/avocado-toast.jpg",
        tags = listOf(
            TagData(
                displayName = "Breakfast",
                type = "meal"
            ),
            TagData(
                displayName = "Vegetarian",
                type = "dietary"
            )
        )
    )

    override suspend fun getRecipes(): Flow<DataResult<List<Recipe>>> = flow {
        emit(DataResult.Loading)

        try {
            // Emit cached data first
//            try {
//                val localToasts = localDataSource.getToasts()
//                emit(Result.Success(localToasts.map(entityToDomainMapper::map)))
//            } catch (e: Exception) {
//                // Handle local data source failure
//            }

            // Fetch fresh data
            val remoteRecipes = remoteDataSource.getRecipes()
            // Save to cache
//            localDataSource.saveToasts(remoteToasts.map(networkToEntityMapper::map))
            // Emit to UI
            emit(DataResult.Success(remoteRecipes.map { it.asDomainModel() }))

        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }
}