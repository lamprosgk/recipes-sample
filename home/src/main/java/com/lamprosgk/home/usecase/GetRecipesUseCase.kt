package com.lamprosgk.home.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(): Flow<Result<List<Recipe>>> =
        recipesRepository.getRecipes().map { result ->
            if (result is Result.Success) {
                Result.Success(
                    result.data.let { recipes ->
                        // put favourites first
                        val (favourites, nonFavourites) = recipes.partition { it.isFavourite }
                        favourites.sortedBy { it.name } + nonFavourites
                    }
                )
            } else {
                result
            }
        }
}