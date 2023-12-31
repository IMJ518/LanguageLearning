package com.example.languagelearning.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.languagelearning.MainActivity

import com.example.languagelearning.api.ApiService
import com.example.languagelearning.api.TranslateResponse
import com.example.languagelearning.ui.components.BtnPlay
import com.example.languagelearning.ui.components.BtnBack
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

var foodTranslation: String = ""
var textToSpeechFood:TextToSpeech? = null

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardFoodScreen(
    foodNames: List<String>,
    foodPhotos: List<String>,
    languageCode: String?,
    navController: NavHostController
) {
    val pageCount = foodNames.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    /**
     * A pager that scrolls horizontally
     */
    HorizontalPager(
        state = pagerState,
        key = { foodNames[it] }
    ) { index ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            var expanded by remember { mutableStateOf(false) }

            Text(
                text = foodNames[index],
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            AsyncImage(
                model = foodPhotos[index],
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
                            translateFoodText(foodNames[index], languageCode)
                        }
                    }
            )

//            Image(
//                painter = painterResource(id = foodPhotos[index]),
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
//                            translateFoodText(foodNames[index], languageCode)
//                        }
//                    }
//            )

            AnimatedVisibility(expanded) {
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = foodTranslation,
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
                                textToSpeechFood(MainActivity.appContext, languageCode)
                            }
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


fun translateFoodText(text: String, languageCode: String?) {
    foodTranslation = ""

    // Send asynchronous HTTP request using enqueue() method (for synchronous request, use execute())
    ApiService.instance.translate(q = text, langpair = "en|$languageCode").enqueue(object : Callback<TranslateResponse> {
        override fun onResponse(call: Call<TranslateResponse>, response: Response<TranslateResponse>) {
            if (response.isSuccessful) {
                val translatedText = response.body()?.responseData?.translatedText

                if (translatedText != null)
                {
                    foodTranslation = translatedText
                    Log.d("translation", foodTranslation)
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

fun textToSpeechFood(context: Context?, languageCode: String?){
    textToSpeechFood = TextToSpeech(
        context
    ){
        if (it == TextToSpeech.SUCCESS){
            textToSpeechFood?.let { txtToSpeech ->
                txtToSpeech.language = Locale.forLanguageTag(languageCode.toString())
                // speed of reading
                txtToSpeech.setSpeechRate(1.0f)
                txtToSpeech.speak(
                    foodTranslation,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null)
            }
        }
    }
}