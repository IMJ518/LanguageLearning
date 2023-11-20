package com.example.languagelearning

import com.example.languagelearning.data.DataSource
import com.example.languagelearning.ui.SelectLanguageScreen
import com.example.languagelearning.ui.FlashCardScreen

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.languagelearning.ui.FlashCardFoodScreen
import com.example.languagelearning.ui.SelectCategoryScreen

enum class Route {
    LanguageSelection,
    CategorySelection,
    FlashCard,
    FlashCardFood
}

@Composable
fun LanguageLearningApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.LanguageSelection.name,
    ) {
        composable(route = Route.LanguageSelection.name) {
            SelectLanguageScreen(
                languageOptions = DataSource.languageCodes,
                flags = DataSource.flags,
                navController = navController
            )
        }

        composable(
            route = "${Route.CategorySelection.name}/{languageCode}",
            arguments = listOf(
                navArgument("languageCode"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            SelectCategoryScreen(
                categoryOptions = DataSource.categoryNames,
                categoryPhotos = DataSource.categoryPhotos,
                languageCode = backStackEntry.arguments?.getString("languageCode"),
                navController = navController
            )
        }

        composable(
            route = "${Route.FlashCard.name}/{languageCode}",
            arguments = listOf(
                navArgument("languageCode"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            FlashCardScreen(
                animalNames = DataSource.animalNames,
                animalPhotos = DataSource.animalPhotos,
                languageCode = backStackEntry.arguments?.getString("languageCode"),
                navController = navController
            )
        }

        composable(
            route = "${Route.FlashCardFood.name}/{languageCode}",
            arguments = listOf(
                navArgument("languageCode"){
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            FlashCardFoodScreen(
                foodNames = DataSource.foodNames,
                foodPhotos = DataSource.foodPhotos,
                languageCode = backStackEntry.arguments?.getString("languageCode"),
                navController = navController
            )
        }
    }
}