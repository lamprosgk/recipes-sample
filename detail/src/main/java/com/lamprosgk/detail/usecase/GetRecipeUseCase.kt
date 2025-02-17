package com.lamprosgk.detail.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(id: Int): Flow<Result<Recipe>> {
        return recipesRepository.getRecipe(id)
    }
}