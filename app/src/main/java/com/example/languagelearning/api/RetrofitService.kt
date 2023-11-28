package com.example.languagelearning.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET

// Data Transfer Object(DTO) - define data structure for API communication
data class TranslateResponse(val responseData: ResponseData) {
    data class ResponseData(
        val translatedText: String
    )
}

// API Interface (define CRUD methods using GET, POST, PUT, DELETE annotations)
interface RetrofitService {
    @GET("get")
    fun translate(@Query("q") q: String, @Query("langpair", encoded = true) langpair: String): Call<TranslateResponse>
}