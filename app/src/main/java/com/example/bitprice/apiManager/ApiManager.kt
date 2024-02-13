package com.example.bitprice.apiManager


import android.util.Log
import com.example.bitprice.BASE_URL
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.apiManager.classData.NewsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ApiManager() {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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


    interface ApiCallback<T> {

        fun onSuccess(data: T)

        fun onError(error: String)
    }
}