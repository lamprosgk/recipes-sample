package com.lamprosgk.detail.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.repository.RecipesRepository
import javax.inject.Inject

class AddToFavouritesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> =
        recipesRepository.addToFavourites(id)
}