package com.example.languagelearning.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScopeInstance.align
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.languagelearning.MainActivity

import com.example.languagelearning.api.ApiService
import com.example.languagelearning.api.TranslateResponse
import com.example.languagelearning.ui.components.BtnPlay
import com.example.languagelearning.ui.components.BtnBack

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

import coil.compose.AsyncImage
import com.example.languagelearning.MainActivity.Companion.appContext
import com.example.languagelearning.ui.components.BtnReview

var translation: String = ""
var textToSpeechAnimals:TextToSpeech? = null
var liked = false

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    animalNames: List<String>,
    animalPhotos: List<String>,
    languageCode: String?,
    navController: NavHostController
) {



//    val animalList: MutableList<String> = mutableListOf()
//    val photoList: MutableList<String> = mutableListOf()
//
//    val db = Firebase.firestore
//    db.collection("flashcards")
//        .whereEqualTo("category", "Animal")
//        .get()
//        .addOnSuccessListener { documents ->
//            for (document in documents) {
//                val cardName = document.data["name"]
//                val cardImage = document.data["image"]
//                animalList.add(cardName.toString())
//                photoList.add(cardImage.toString())
//
////                Log.d("DBtest", cardName.toString())
//            }
//            Log.d("LionName", animalList[0])
//            Log.d("TigerName", animalList[1])
//            Log.d("LionPhoto", photoList[0])
//            Log.d("TigerPhoto", photoList[1])
//        }



    val pageCount = animalNames.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

//    Log.d("Page Count", pageCount.toString())

    /**
     * A pager that scrolls horizontally
     */
    HorizontalPager(
        state = pagerState,
        key = { animalNames[it] }
    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            var expanded by remember { mutableStateOf(false) }

            Text(
                text = animalNames[index],
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            AsyncImage(
                model = animalPhotos[index],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .requiredSize(350.dp)
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                        if (expanded) {
                            Log.d("test", "expanded")
                            translateText(animalNames[index], languageCode)
                        }
                    }
            )
//            Image(
//                painter = painterResource(id = animalPhotos[index]),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .requiredSize(350.dp)
//                    .padding(10.dp)
//                    .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth()
//                    .clickable {
//                        expanded = !expanded
//                        if (expanded) {
//                            Log.d("test", "expanded")
//                            translateText(animalList[index], languageCode)
//                        }
//                    }
//            )

            AnimatedVisibility(expanded) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = translation,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Row {
                        BtnPlay(onClick = {
                            if (languageCode != null) {
                                textToSpeechAnimals(MainActivity.appContext, languageCode)
                            }
                        })
                        BtnReview(onClick = {
                            Toast.makeText(appContext, "Added to reviews.", Toast.LENGTH_SHORT).show()
                            // Write on the db
                        })
                    }

                }
            }
        }
    }


    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom,
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(20.dp)
            )
        }
    }

    BtnBack(onClick = { navController.navigateUp() })
}

fun translateText(text: String, languageCode: String?) {
    translation = ""

    // Send asynchronous HTTP request using enqueue() method (for synchronous request, use execute())
    ApiService.instance.translate(q = text, langpair = "en|$languageCode").enqueue(object : Callback<TranslateResponse>{
        override fun onResponse(call: Call<TranslateResponse>, response: Response<TranslateResponse>) {
            if (response.isSuccessful) {
                val translatedText = response.body()?.responseData?.translatedText

                if (translatedText != null)
                {
                    translation = translatedText
                    Log.d("translation", translation)
                }
            } else {
                // Handle error
                Log.d("test", "no response $languageCode")
            }
        }

        override fun onFailure(call: Call<TranslateResponse>, t: Throwable) {
            // Handle failure of the request
            Log.d("test", "request failed")
        }
    })
}

fun textToSpeechAnimals(context: Context?,  languageCode: String){
    textToSpeechAnimals = TextToSpeech(
        context
    ){

        if (it == TextToSpeech.SUCCESS){
            textToSpeechAnimals?.let { txtToSpeech ->
                txtToSpeech.language = Locale.forLanguageTag(languageCode)
                // speed of reading
                txtToSpeech.setSpeechRate(1.0f)
                txtToSpeech.speak(
                    translation,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null)
            }
        }
    }
}

