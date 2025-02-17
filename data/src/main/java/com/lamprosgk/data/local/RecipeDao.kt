package com.lamprosgk.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface RecipeDao {
    @Transaction
    suspend fun upsertRecipes(recipes: List<RecipeEntity>) {
        recipes.forEach { recipe ->
            val existingRecipe = getRecipe(recipe.id).firstOrNull()
            if (existingRecipe == null) {
                insertRecipe(recipe)
            } else {
                updateRecipe(
                    recipe.id,
                    recipe.name,
                    recipe.description,
                    recipe.instructionsSteps,
                    recipe.prepTimeMinutes,
                    recipe.totalTimeMinutes,
                    recipe.numServings,
                    recipe.calories,
                    recipe.thumbnailUrl,
                    recipe.tags
                )
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE) // ignoring for initial insertions
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("UPDATE recipes SET name = :name, description = :description, instructionsSteps = :instructionsSteps, prepTimeMinutes = :prepTimeMinutes, totalTimeMinutes = :totalTimeMinutes, numServings = :numServings, calories = :calories, thumbnailUrl = :thumbnailUrl, tags = :tags WHERE id = :id")
    suspend fun updateRecipe(
        id: Int,
        name: String,
        description: String,
        instructionsSteps: String,
        prepTimeMinutes: Int,
        totalTimeMinutes: Int,
        numServings: Int,
        calories: Int,
        thumbnailUrl: String,
        tags: String
    )

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipe(recipeId: Int): Flow<RecipeEntity>

    @Query("UPDATE recipes SET isFavourite = 1 WHERE id = :recipeId")
    suspend fun addToFavourites(recipeId: Int)

    @Query("UPDATE recipes SET isFavourite = 0 WHERE id = :recipeId")
    suspend fun removeFromFavourites(recipeId: Int)
}