package com.example.bitprice

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bitprice.apiManager.ApiManager
import com.example.bitprice.apiManager.classData.CoinAboutData
import com.example.bitprice.apiManager.classData.CoinItem
import com.example.bitprice.apiManager.classData.CoinsData


import com.example.bitprice.databinding.ActivityMarketBinding
import com.google.gson.Gson


class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallback {
    private lateinit var binding: ActivityMarketBinding
    private val apiManager = ApiManager()
    private lateinit var dataMap: MutableMap<String, CoinItem>
    lateinit var newsData: ArrayList<Pair<String, String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarMain.toolbar.title = "BitPrice"

        binding.watchlist.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://coinmarketcap.com/"))
            startActivity(intent)
        }
        binding.refresh.setOnRefreshListener {
            ui()
            Handler(Looper.getMainLooper()).postDelayed(
                { binding.refresh.isRefreshing = false }, 1500
            )
        }


        getAboutCoin()

    }


    override fun onResume() {
        super.onResume()
        ui()
    }

    private fun ui() {

        getNewsFromApi()
        getTopCoins()
    }

    private fun getAboutCoin() {

        val fileInString = applicationContext.assets.open("currencyinfo.json").bufferedReader()
            .use { it.readText() }


        dataMap = mutableMapOf()


        val dataAbout = Gson().fromJson(fileInString, CoinAboutData::class.java)


        dataAbout.forEach {


            dataMap[it.currencyName] = CoinItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit

            )

        }


    }


    private fun getNewsFromApi() {
        apiManager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                newsData = data
                refreshNews()
            }

            override fun onError(error: String) {
                Toast.makeText(this@MarketActivity, error, Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun refreshNews() {
        val randomNews = (0..49).random()

        binding.news.txtNews.text = newsData[randomNews].first

        binding.news.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsData[randomNews].second))
            startActivity(intent)
        }


        binding.news.txtNews.setOnClickListener { refreshNews() }
    }


    private fun cleanData(data: List<CoinsData.Data>): List<CoinsData.Data> {
        val purifyData = mutableListOf<CoinsData.Data>()

        data.forEach {
            if (it.rAW != null || it.dISPLAY != null) {

                purifyData.add(it)
            }
        }

        return purifyData
    }


    private fun getTopCoins() {
        apiManager.getCoins(object : ApiManager.ApiCallback<List<CoinsData.Data>> {
            override fun onSuccess(data: List<CoinsData.Data>) {

                showDataINRecycler(cleanData(data))

            }

            override fun onError(error: String) {
                Toast.makeText(this@MarketActivity, error, Toast.LENGTH_SHORT).show()
                Log.v("bug", error)

            }


        })
    }


    private fun showDataINRecycler(data: List<CoinsData.Data>) {
        val marketAdapter = MarketAdapter(ArrayList(data), this)
        binding.watchlist.recyclerView.adapter = marketAdapter
        binding.watchlist.recyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun coinClick(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)


        val bundle = Bundle()
        bundle.putParcelable(Data1, dataCoin)
        bundle.putParcelable(Data2, dataMap[dataCoin.coinInfo.name])

        intent.putExtra(DataTransfer, bundle)





        startActivity(intent)

    }


}