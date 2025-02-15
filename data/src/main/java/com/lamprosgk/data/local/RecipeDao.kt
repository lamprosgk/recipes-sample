package com.lamprosgk.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("UPDATE recipes SET isFavourite = 1 WHERE id = :recipeId")
    suspend fun markAsFavorite(recipeId: Int)

    @Query("UPDATE recipes SET isFavourite = 0 WHERE id = :recipeId")
    suspend fun unmarkAsFavorite(recipeId: Int)
}