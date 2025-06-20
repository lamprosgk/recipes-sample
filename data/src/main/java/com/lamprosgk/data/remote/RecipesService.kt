package com.lamprosgk.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface RecipesService {
    @GET("list")
    suspend fun getRecipes(): Response<RecipeResponse>
}