package com.lamprosgk.ui.viewmodel

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.home.ui.screens.RecipesEvent
import com.lamprosgk.home.ui.screens.RecipesIntent
import com.lamprosgk.home.ui.screens.RecipesViewModel
import com.lamprosgk.home.ui.screens.RecipesViewState
import com.lamprosgk.home.usecase.GetRecipesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var getRecipesUseCase: GetRecipesUseCase
    private lateinit var viewModel: RecipesViewModel

    private val sampleRecipes = listOf(
        Recipe(
            id = 1,
            name = "Italian Pasta",
            description = "Description 1",
            ingredients = listOf("ingredient1", "ingredient2"),
            instructionsSteps = listOf("step1", "step2"),
            prepTimeMinutes = 10,
            totalTimeMinutes = 20,
            numServings = 2,
            calories = 300,
            thumbnailUrl = "https://www.example.com/image.jpg",
            tags = listOf("Italian", "Quick"),
            isFavourite = true
        ),
        Recipe(
            id = 2,
            name = "Asian Stir Fry",
            description = "Description 2",
            ingredients = listOf("ingredient1"),
            instructionsSteps = listOf("step1"),
            prepTimeMinutes = 15,
            totalTimeMinutes = 30,
            numServings = 4,
            calories = 400,
            thumbnailUrl = "https://www.example.com/image.jpg",
            tags = listOf("Asian", "Healthy"),
            isFavourite = false
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getRecipesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        coEvery { getRecipesUseCase() } returns flowOf(Result.Loading)

        viewModel = RecipesViewModel(getRecipesUseCase)

        assertTrue(viewModel.state.value is RecipesViewState.Loading)
    }

    @Test
    fun `when recipes fetched successfully state updates to Success`() = runTest {
        coEvery { getRecipesUseCase() } returns flowOf(Result.Success(sampleRecipes))

        viewModel = RecipesViewModel(getRecipesUseCase)

        assertTrue(viewModel.state.value is RecipesViewState.Success)
        assertEquals(
            sampleRecipes,
            (viewModel.state.value as RecipesViewState.Success).recipes
        )
    }

    @Test
    fun `when error occurs while fetching recipes state updates to Error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getRecipesUseCase() } returns flowOf(Result.Error(Exception(errorMessage)))

        viewModel = RecipesViewModel(getRecipesUseCase)

        assertTrue(viewModel.state.value is RecipesViewState.Error)
        assertEquals(
            errorMessage,
            (viewModel.state.value as RecipesViewState.Error).message
        )
    }

    @Test
    fun `when a recipe is clicked recipe NavigateToDetail event is emitted with correct id`() = runTest {
        coEvery { getRecipesUseCase() } returns flowOf(Result.Success(sampleRecipes))
        viewModel = RecipesViewModel(getRecipesUseCase)

        var capturedEvent: RecipesEvent? = null
        testScope.launch {
            viewModel.events.collect { event ->
                capturedEvent = event
                cancel()
            }
        }

        viewModel.onIntent(RecipesIntent.RecipeClickedIntent(1))

        assertTrue(capturedEvent is RecipesEvent.NavigateToDetail)
        assertEquals(1, (capturedEvent as RecipesEvent.NavigateToDetail).recipeId)
    }

    @Test
    fun `when flow exception occurs state is updated to Error`() = runTest {
        val errorMessage = "Test exception"
        coEvery { getRecipesUseCase() } returns flowOf(Result.Error(Exception(errorMessage)))

        viewModel = RecipesViewModel(getRecipesUseCase)
        testScheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value is RecipesViewState.Error)
        assertEquals(
            errorMessage,
            (viewModel.state.value as RecipesViewState.Error).message
        )
    }
}