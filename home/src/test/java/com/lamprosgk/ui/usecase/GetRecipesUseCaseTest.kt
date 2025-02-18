package com.lamprosgk.ui.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import com.lamprosgk.home.usecase.GetRecipesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetRecipesUseCaseTest {

    private lateinit var recipesRepository: RecipesRepository
    private lateinit var getRecipesUseCase: GetRecipesUseCase

    @Before
    fun setup() {
        recipesRepository = mockk()
        getRecipesUseCase = GetRecipesUseCase(recipesRepository)
    }

    @Test
    fun `when items are successfully returned, favourites are sorted first`() = runTest {
        val recipes = listOf(
            Recipe(
                id = 1,
                name = "Pizza",
                description = "Classic Pizza",
                ingredients = listOf("Dough", "Tomato sauce"),
                instructionsSteps = listOf("Step 1", "Step 2"),
                prepTimeMinutes = 30,
                totalTimeMinutes = 45,
                numServings = 4,
                calories = 800,
                thumbnailUrl = "https://www.example.com/image.jpg",
                tags = listOf("Italian"),
                isFavourite = false
            ),
            Recipe(
                id = 2,
                name = "Burger",
                description = "Classic Burger",
                ingredients = listOf("Bun", "Patty"),
                instructionsSteps = listOf("Step 1", "Step 2"),
                prepTimeMinutes = 20,
                totalTimeMinutes = 30,
                numServings = 1,
                calories = 600,
                thumbnailUrl = "https://www.example.com/image.jpg",
                tags = listOf("American"),
                isFavourite = true
            ),
            Recipe(
                id = 3,
                name = "Pasta",
                description = "Classic Pasta",
                ingredients = listOf("Pasta", "Sauce"),
                instructionsSteps = listOf("Step 1", "Step 2"),
                prepTimeMinutes = 15,
                totalTimeMinutes = 25,
                numServings = 2,
                calories = 500,
                thumbnailUrl = "https://www.example.com/image.jpg",
                tags = listOf("Italian"),
                isFavourite = true
            )
        )

        coEvery { recipesRepository.getRecipes() } returns flowOf(Result.Success(recipes))

        val results = getRecipesUseCase().toList()

        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Success)

        val sortedRecipes = (results[0] as Result.Success<List<Recipe>>).data
        assertEquals(3, sortedRecipes.size)

        assertTrue(sortedRecipes[0].isFavourite)
        assertTrue(sortedRecipes[1].isFavourite)
        assertEquals("Burger", sortedRecipes[0].name)
        assertEquals("Pasta", sortedRecipes[1].name)
        assertTrue(!sortedRecipes[2].isFavourite)
        assertEquals("Pizza", sortedRecipes[2].name)
    }

    @Test
    fun `when repository returns error, error is propagated`() = runTest {
        val error = Exception("Network error")
        coEvery { recipesRepository.getRecipes() } returns flowOf(Result.Error(error))

        val results = getRecipesUseCase().toList()

        assertEquals(1, results.size)
        assertTrue(results[0] is Result.Error)
        assertEquals(error, (results[0] as Result.Error).exception)
    }
}