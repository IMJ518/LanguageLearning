package com.example.languagelearning.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create API service object
object ApiService {
    private const val BASE_URL = "https://api.mymemory.translated.net/" // base url of API, it must end with '/'

    // Retrofit interface instance
    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // convert JSON response into Gson
            .build()

        // API service instance
        retrofit.create(RetrofitService::class.java)
    }
}
