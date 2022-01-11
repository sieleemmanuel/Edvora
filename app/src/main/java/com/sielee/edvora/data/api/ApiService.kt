package com.sielee.edvora.data.api

import com.sielee.edvora.data.models.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getProducts():List<Product>
}
private val BASE_URL = "https://assessment-edvora.herokuapp.com"

private val logger = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}
private val client = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

object ProductApi{
    val apiService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

}