package com.lamprosgk.recipes.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lamprosgk.detail.nav.RecipeDetailNavArgs
import com.lamprosgk.detail.ui.screens.RecipeDetailScreen
import com.lamprosgk.detail.ui.screens.RecipeDetailViewModel
import com.lamprosgk.home.ui.screens.RecipesEvent
import com.lamprosgk.home.ui.screens.RecipesScreen
import com.lamprosgk.home.ui.screens.RecipesViewModel
import com.lamprosgk.recipes.nav.NavRoutes.RECIPES
import com.lamprosgk.recipes.nav.NavRoutes.RECIPE_DETAIL


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Recipes.route,
        modifier = modifier
    ) {
        composable(NavDestination.Recipes.route) {
            val viewModel: RecipesViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            RecipesScreen(state = state, onIntent = viewModel::onIntent)

            LaunchedEffect(Unit) {
                viewModel.events.collect { event ->
                    when (event) {
                        is RecipesEvent.NavigateToDetail -> {
                            navController.navigate(
                                NavDestination.RecipeDetail.routeWithArgs(event.recipeId)
                            ) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            }
        }

        composable(
            route = NavDestination.RecipeDetail.route,
            arguments = listOf(
                navArgument(NavArgs.RECIPE_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            val viewModel: RecipeDetailViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            RecipeDetailScreen(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}

object NavRoutes {
    const val RECIPES = "recipes"
    const val RECIPE_DETAIL = "recipe_detail/{${NavArgs.RECIPE_ID}}"
}

object NavArgs {
    const val RECIPE_ID = RecipeDetailNavArgs.RECIPE_ID_ARG
}

sealed class NavDestination(val route: String) {
    data object Recipes : NavDestination(RECIPES)
    data object RecipeDetail : NavDestination(RECIPE_DETAIL) {
        fun routeWithArgs(recipeId: Int) =
            route.replace("{${NavArgs.RECIPE_ID}}", recipeId.toString())
    }
}