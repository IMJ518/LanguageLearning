package com.example.languagelearning

import android.annotation.SuppressLint
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import com.example.languagelearning.data.DataSource
import com.example.languagelearning.ui.SelectLanguageScreen
import com.example.languagelearning.ui.FlashCardScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.languagelearning.ui.FlashCardFoodScreen
import com.example.languagelearning.ui.SelectCategoryScreen
import com.example.languagelearning.ui.textToSpeechAnimals
import com.example.languagelearning.ui.textToSpeechFood
import com.example.languagelearning.ui.translation
import java.util.Locale

enum class Route {
    LanguageSelection,
    CategorySelection,
    FlashCard,
    FlashCardFood
}

data class BottomNavigationItem(
    val title: String,
    val link: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageLearningApp() {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            link = Route.LanguageSelection.name,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Favorites",
            link = Route.LanguageSelection.name,
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        ),
        BottomNavigationItem(
            title = "Add Flashcard",
            link = Route.LanguageSelection.name,
            selectedIcon = Icons.Filled.AddCircle,
            unselectedIcon = Icons.Outlined.Add
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){}
    Scaffold (
        bottomBar = {
            androidx.compose.material3.NavigationBar() {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selectedItemIndex == index, {
                            selectedItemIndex = index
                            navController.navigate(item.link)
                        }, {
                            Icon(
                                imageVector = if(index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(text = item.title)
                        }
                    )
                }
            }
        }
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
            ) {
                SelectCategoryScreen(
                    categoryOptions = DataSource.categoryNames,
                    categoryPhotos = DataSource.categoryPhotos,
                    languageCode = backStackEntry?.arguments?.getString("languageCode"),
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
            ) {
                FlashCardScreen(
                    animalNames = DataSource.animalNames,
                    animalPhotos = DataSource.animalPhotos,
                    languageCode = backStackEntry?.arguments?.getString("languageCode"),
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
            ) {
                FlashCardFoodScreen(
                    foodNames = DataSource.foodNames,
                    foodPhotos = DataSource.foodPhotos,
                    languageCode = backStackEntry?.arguments?.getString("languageCode"),
                    navController = navController
                )
            }
        }
    }
}