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
                // sort with favourites first and then by name
                Result.Success(
                    result.data.sortedWith(
                        compareByDescending<Recipe> { it.isFavourite }.thenBy { it.name }
                    )
                )
            } else {
                result
            }
        }
}