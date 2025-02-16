package com.lamprosgk.domain.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    operator fun invoke(): Flow<Result<List<Recipe>>> {
        return recipesRepository.getRecipes()
    }
}