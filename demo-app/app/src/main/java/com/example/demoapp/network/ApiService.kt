package com.example.demoapp.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("/feedback")
    suspend fun sendFeedback(@Body data: Map<String, String>): Response<Map<String, String>>
}