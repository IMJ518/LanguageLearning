package com.example.languagelearning.ui

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

@Composable
fun SelectLanguageScreen(
    languageOptions: List<String>,
    flags: List<Int>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(languageOptions.size) {  index ->
            val languageCode = DataSource.languageCodes[index]

            Image(
                painter = painterResource(id = flags[index]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(200.dp)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = { navController.navigate("FlashCard/${languageCode}") }
                    )

            )
        }
    }
}