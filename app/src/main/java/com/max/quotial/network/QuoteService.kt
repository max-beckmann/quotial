package com.max.quotial.network

import retrofit2.http.GET

data class QuoteOfTheDay(
    val q: String,
    val a: String,
    val h: String,
) {}

interface QuoteService {
    @GET("?api=today")
    suspend fun getQuoteOfTheDay(): List<QuoteOfTheDay>
}