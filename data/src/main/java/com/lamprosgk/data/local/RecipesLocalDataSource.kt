package com.lamprosgk.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecipesLocalDataSource {
    fun getAllRecipes(): Flow<List<RecipeEntity>> // flow to observe for db changes
    fun getRecipe(id: Int): Flow<RecipeEntity>
    suspend fun insertRecipes(recipes: List<RecipeEntity>)
    suspend fun markAsFavorite(recipeId: Int)
    suspend fun unmarkAsFavorite(recipeId: Int)
}

class RecipesLocalDataSourceImpl @Inject constructor(private val recipeDao: RecipeDao) :
    RecipesLocalDataSource {

    override fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    override suspend fun insertRecipes(recipes: List<RecipeEntity>) =
        recipeDao.insertRecipes(recipes)

    override fun getRecipe(id: Int): Flow<RecipeEntity> = recipeDao.getRecipe(id)

    override suspend fun markAsFavorite(recipeId: Int) = recipeDao.markAsFavorite(recipeId)

    override suspend fun unmarkAsFavorite(recipeId: Int) = recipeDao.unmarkAsFavorite(recipeId)
}