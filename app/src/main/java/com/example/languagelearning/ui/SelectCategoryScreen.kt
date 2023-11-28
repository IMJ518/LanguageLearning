package com.example.languagelearning.ui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(categoryOptions.size) {  index ->
            val categorySelected = DataSource.categoryNames[index]

            Image(
                painter = painterResource(id = categoryPhotos[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(250.dp)
                    .padding(20.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .border(3.dp, Color.LightGray, CircleShape)
                    .border(5.dp, Color.White, CircleShape)
                    .clickable(
                        onClick = {
                            if (categorySelected == "Animals")
                            {
                                navController.navigate("FlashCard/${languageCode}")
                                Log.d("Category", categorySelected)
                            }
                            else if (categorySelected == "Food")
                            {
                                navController.navigate("FlashCardFood/${languageCode}")
                                Log.d("Category", categorySelected)
                            }
                        }
                    )

            )
        }
    }

    BtnBack(onClick = { navController.navigateUp() })
}