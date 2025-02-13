package com.lamprosgk.data

import retrofit2.HttpException
import javax.inject.Inject

interface RecipesRemoteDataSource {
    suspend fun getRecipes(): List<RecipeData>
}

class RecipesRemoteDataSourceImpl @Inject constructor(
    private val recipesService: RecipesService
)  : RecipesRemoteDataSource {
    override suspend fun getRecipes(): List<RecipeData> {
        val response = recipesService.getRecipes()
        if (response.isSuccessful) {
            return response.body()?.results ?: throw Exception("Response is null")
        } else {
            throw HttpException(response)
        }
    }
}