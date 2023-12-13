package com.example.languagelearning

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.languagelearning.data.DataSource
import com.example.languagelearning.ui.SelectLanguageScreen
import com.example.languagelearning.ui.FlashCardScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.languagelearning.ui.AnimalCardViewModel
import com.example.languagelearning.ui.FlashCardFoodScreen
import com.example.languagelearning.ui.FoodCardViewModel
import com.example.languagelearning.ui.SelectCategoryScreen
import com.example.languagelearning.ui.UploadFlashCardForm
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

enum class Route {
    LanguageSelection,
    CategorySelection,
    FlashCard,
    FlashCardFood,
    UploadFlashCard,
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

    // Update FlashCardState when db updated
    val db = Firebase.firestore

    // Detect animal flash card changes
    val animalCardViewModel: AnimalCardViewModel = viewModel()
    val animalUiState by animalCardViewModel.AnimalUiState.collectAsState()

    db.collection("flashcards")
        .whereEqualTo("category", "Animal")
        .addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("DB Error", "Listen failed.", e)
                return@addSnapshotListener
            }

            val animalNames = ArrayList<String>()
            val animalPhotos = ArrayList<String>()

            for (doc in documents!!) {
                doc.getString("name")?.let {
                    animalNames.add(it)
                }
                doc.getString("image")?.let {
                    animalPhotos.add(it)
                }
            }
            Log.d("Test", "Animals: $animalNames")

            animalCardViewModel.setAnimalFlashCard("Animal", animalNames, animalPhotos)
        }

    // Detect food flash card changes
    val foodCardViewModel: FoodCardViewModel = viewModel()
    val foodUiState by foodCardViewModel.FoodUiState.collectAsState()

    db.collection("flashcards")
        .whereEqualTo("category", "Food")
        .addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w("DB Error", "Listen failed.", e)
                return@addSnapshotListener
            }

            val foodNames = ArrayList<String>()
            val foodPhotos = ArrayList<String>()

            for (doc in documents!!) {
                doc.getString("name")?.let {
                    foodNames.add(it)
                }
                doc.getString("image")?.let {
                    foodPhotos.add(it)
                }
            }
            Log.d("Test", "Foods: $foodNames")

            foodCardViewModel.setFoodFlashCard("Food", foodNames, foodPhotos)
        }




    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            link = Route.LanguageSelection.name,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Add Flashcard",
            link = Route.UploadFlashCard.name,
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
                    animalNames = animalUiState.animalNames,
                    animalPhotos = animalUiState.animalPhotos,
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
                    foodNames = foodUiState.foodNames,
                    foodPhotos = foodUiState.foodPhotos,
                    languageCode = backStackEntry?.arguments?.getString("languageCode"),
                    navController = navController
                )
            }

            composable(route = Route.UploadFlashCard.name) {
                UploadFlashCardForm(
                    navController = navController
                )
            }
        }
    }
}