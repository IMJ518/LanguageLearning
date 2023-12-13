package com.example.languagelearning.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextFieldColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.languagelearning.data.FlashCard
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

@Composable
fun UploadFlashCardForm(
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val items = listOf("Animal", "Food")
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Animal") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Category",
            modifier = Modifier
                .width(350.dp)
                .padding(5.dp, 10.dp),
            fontSize = 20.sp
        )

        Box(
            modifier = Modifier
                .width(350.dp)
                .padding(0.dp, 10.dp))
        {
            Text(
                items[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .background(Color.LightGray)
                    .padding(15.dp, 15.dp),
                fontSize = 20.sp)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(350.dp)
                    .background(Color.White)
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCategory = s
                            selectedIndex = index
                            expanded = false
                        }) {
                        Text(text = s)
                    }
                }
            }
        }

        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .width(350.dp)
                .padding(0.dp, 10.dp),
            label = { Text("Name") }
        )

        TextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            modifier = Modifier
                .width(350.dp)
                .padding(0.dp, 10.dp),
            label = { Text("Image URL") }
        )

        Row(
            modifier = Modifier
                .padding(0.dp, 10.dp)
                .width(350.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(
                onClick = {
                    AddNewFlashCard(name, selectedCategory, imageUrl)


                    navController.navigateUp()}
            ) {
                Text("Upload")
            }

            Button(
                onClick = {
                    navController.navigateUp()}
            ) {
                Text("Cancel")
            }
        }
    }
}

fun AddNewFlashCard(name: String, category: String, image: String){
    val database = Firebase.firestore
    val card = FlashCard(name, category, image)
    database.collection("flashcards").add(card)
}