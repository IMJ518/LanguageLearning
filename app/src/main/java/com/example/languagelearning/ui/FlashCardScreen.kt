package com.example.languagelearning.ui

import android.util.Log
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
import com.example.languagelearning.api.ApiService
import com.example.languagelearning.ui.components.BtnBack
import com.example.languagelearning.ui.components.BtnPlay
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var translation: String = ""

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashCardScreen(
    animalNames: List<String>,
    animalPhotos: List<Int>,
    languageCode: String?,
    navController: NavHostController
) {
    val pageCount = animalNames.size
    val pagerState = rememberPagerState()

    /**
     * A pager that scrolls horizontally
     */
    HorizontalPager(
        pageCount = pageCount,
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

            Image(
                painter = painterResource(id = animalPhotos[index]),
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
                    BtnPlay(onClick = { Log.d("test", "clicked") })
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
    ApiService.instance.translate(q = text, langpair = "en|$languageCode").enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                Log.d("test", response.body()!!.string())
                // Handle data
//                val translatedText = response.body()
//                if (translatedText != null)
//                {
//                    translation = translatedText
//                    Log.d("test", translation) // print the translated text in logcat
//                }
            } else {
                // Handle error
                Log.d("test", "no response $languageCode")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            // Handle failure of the request
            Log.d("test", "request failed")
        }
    })
}