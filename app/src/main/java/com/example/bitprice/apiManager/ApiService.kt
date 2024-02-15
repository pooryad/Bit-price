package com.example.bitprice.apiManager

import com.example.bitprice.API_KEY
import com.example.bitprice.apiManager.classData.ChartData
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.apiManager.classData.NewsData
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrder") sortOrder: String = "popular"
    ): Call<NewsData>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTopCoins(
        @Query("tsym") symbol: String = "USD",
        @Query("limit") limitData: Int = 15
    ): Call<CoinsData>


    @Headers(API_KEY)
    @GET("{period}")
    fun getChartData(
        @Path("period") period: String,
        @Query("fsym") fromSymbol: String,
        @Query("limit") limit: Int,
        @Query("aggregate") aggregate: Int,
        @Query("tsym") toSymbol: String = "USD"
    ):Call<ChartData>


}