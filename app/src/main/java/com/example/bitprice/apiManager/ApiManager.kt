package com.example.bitprice.apiManager


import android.util.Log
import com.example.bitprice.ALL
import com.example.bitprice.BASE_URL

import com.example.bitprice.HISTO_DAY
import com.example.bitprice.HISTO_HOUR
import com.example.bitprice.HISTO_MINUTE
import com.example.bitprice.HOUR
import com.example.bitprice.HOURS24
import com.example.bitprice.MONTH
import com.example.bitprice.MONTH3
import com.example.bitprice.WEEK
import com.example.bitprice.YEAR
import com.example.bitprice.apiManager.classData.ChartData
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.apiManager.classData.NewsData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiManager {
    private val apiService: ApiService

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }


    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {

        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                val data = response.body()!!


                val sendData: ArrayList<Pair<String, String>> = arrayListOf()
                data.data.forEach {
                    sendData.add(Pair(it.title, it.url))
                }
                apiCallback.onSuccess(sendData)
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
            }

        })

    }

    fun getCoins(apiCallback: ApiCallback<List<CoinsData.Data>>) {
        apiService.getTopCoins().enqueue(object : Callback<CoinsData> {
            override fun onResponse(call: Call<CoinsData>, response: Response<CoinsData>) {
                val data = response.body()!!
                apiCallback.onSuccess(data.data)
            }

            override fun onFailure(call: Call<CoinsData>, t: Throwable) {
                apiCallback.onError(t.message!!)
                Log.v("pari", "Line off error${t.message}")

            }


        })


    }


    fun getChart(
        symbol: String,
        period: String,
        apiCallback: ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>>
    ) {

        var historyPeriod = ""
        var limit = 30
        var aggregate = 1
        when (period) {

            HOUR -> {
                historyPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {
                historyPeriod = HISTO_HOUR
                limit = 24
            }

            WEEK -> {
                historyPeriod = HISTO_HOUR
                aggregate = 6
            }

            MONTH -> {
                historyPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                historyPeriod = HISTO_DAY
                limit = 90

            }

            YEAR -> {
                historyPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                historyPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }

        }




        apiService.getChartData(historyPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {


                    val fullData = response.body()!!

                    val dataOne = fullData.data


                    val dataTwo = fullData.data.maxByOrNull {
                        it.close.toFloat()
                    }


                    val returnData = Pair(dataOne, dataTwo)


                    apiCallback.onSuccess(returnData)

                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }

            })


    }


    interface ApiCallback<T> {

        fun onSuccess(data: T)

        fun onError(error: String)
    }
}