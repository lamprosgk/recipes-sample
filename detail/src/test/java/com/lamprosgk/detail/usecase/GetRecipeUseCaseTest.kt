package com.lamprosgk.detail.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecipeUseCaseTest {

    private lateinit var recipesRepository: RecipesRepository
    private lateinit var getRecipeUseCase: GetRecipeUseCase

    @Before
    fun setup() {
        recipesRepository = mockk()
        getRecipeUseCase = GetRecipeUseCase(recipesRepository)
    }

    @Test
    fun `invoke() returns recipe when repository returns success`() = runTest {
        val recipeId = 1
        val recipe = Recipe(
            id = recipeId,
            name = "Pizza",
            description = "Classic Pizza",
            ingredients = listOf("Dough", "Tomato sauce"),
            instructionsSteps = listOf("Step 1", "Step 2"),
            prepTimeMinutes = 30,
            totalTimeMinutes = 45,
            numServings = 4,
            calories = 800,
            thumbnailUrl = "https://www.example.com/pizza.jpg",
            tags = listOf("Italian"),
            isFavourite = false
        )
        coEvery { recipesRepository.getRecipe(recipeId) } returns flowOf(Result.Success(recipe))

        val results = getRecipeUseCase(recipeId).toList()

        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Success)
        assertEquals(recipe, (results[0] as Result.Success<Recipe>).data)
        verify(exactly = 1) { recipesRepository.getRecipe(recipeId) }
    }

    @Test
    fun `invoke() propagates error when repository returns error`() = runTest {
        val recipeId = 1
        val error = Exception("Recipe not found")
        coEvery { recipesRepository.getRecipe(recipeId) } returns flowOf(Result.Error(error))

        val results = getRecipeUseCase(recipeId).toList()

        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Error)
        assertEquals(error, (results[0] as Result.Error).exception)
        verify(exactly = 1) { recipesRepository.getRecipe(recipeId) }
    }
}