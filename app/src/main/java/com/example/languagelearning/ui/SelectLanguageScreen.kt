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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(languageOptions.size) {  index ->
            val languageCode = DataSource.languageCodes[index]

            Image(
                painter = painterResource(id = flags[index]),
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
                            navController.navigate("CategorySelection/${languageCode}")
                            Log.d("LanguageSelected", languageCode)
                        }
                    )

            )
        }
    }
}