package com.example.languagelearning.ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.example.languagelearning.data.DataSource
import com.example.languagelearning.ui.components.BtnBack

@Composable
fun SelectCategoryScreen(
    categoryOptions: List<String>,
    categoryPhotos: List<Int>,
    languageCode: String?,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(categoryOptions.size) {  index ->
            val categorySelected = DataSource.categoryNames[index]
            val languageSelected = languageCode

            Image(
                painter = painterResource(id = categoryPhotos[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(200.dp)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            if (categorySelected == "Animals")
                            {
                                navController.navigate("FlashCard/${languageSelected}")
                                Log.d("Category", categorySelected)
                            }
                            else if (categorySelected == "Food")
                            {
                                navController.navigate("FlashCardFood/${languageSelected}")
                                Log.d("Category", categorySelected)
                            }
                        }
                    )

            )
        }
    }

    BtnBack(onClick = { navController.navigateUp() })
}