package com.example.bitprice


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bitprice.apiManager.ApiManager
import com.example.bitprice.apiManager.classData.ChartData
import com.example.bitprice.apiManager.classData.CoinItem
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.databinding.ActivityCoinBinding


@Suppress("DEPRECATION")
class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataInformation: CoinsData.Data
    private lateinit var aboutCoin: CoinItem
    val apiManager = ApiManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fromIntent = intent.getBundleExtra(DataTransfer)!!
        dataInformation = fromIntent.getParcelable(Data1)!!

        aboutCoin = if (fromIntent.getParcelable<CoinItem>(Data2) != null) {

            fromIntent.getParcelable(Data2)!!
        } else {

            CoinItem()
        }


        binding.toolbarCoin.toolbar.title = dataInformation.coinInfo.fullName
        uiCoin()


    }

    private fun uiCoin() {
        chartShow()
        coinStatistics()
        coinAbout()
    }


    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Address Not Available", Toast.LENGTH_SHORT).show()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun coinAbout() {

        binding.about.txtWebsite.text = aboutCoin.coinWebsite
        binding.about.txtGithub.text = aboutCoin.coinGit
        binding.about.txtTwitter.text = "@" + aboutCoin.coinTwitter
        binding.about.txtReddit.text = aboutCoin.coinReddit
        binding.about.txtAboutCoin.text = aboutCoin.coinDesc


        binding.about.txtWebsite.setOnClickListener {
            openLink(aboutCoin.coinWebsite!!)

        }
        binding.about.txtGithub.setOnClickListener {
            if (aboutCoin.coinGit != null && aboutCoin.coinGit != "no-data") {
                openLink(aboutCoin.coinGit!!)
            } else {
                Toast.makeText(this, "GitHub Address not available", Toast.LENGTH_SHORT).show()
            }
        }



        binding.about.txtTwitter.setOnClickListener {
            if (!aboutCoin.coinTwitter.isNullOrEmpty() && aboutCoin.coinTwitter != "no-data") {
                val twitterHandle = "@" + aboutCoin.coinTwitter
                val twitterUrl = TWITTER_URL + aboutCoin.coinTwitter

                binding.about.txtTwitter.text = twitterHandle

                openLink(twitterUrl)
            } else {
                Toast.makeText(this, "Twitter Address not available", Toast.LENGTH_SHORT).show()
            }
        }




        binding.about.txtReddit.setOnClickListener {
            openLink(aboutCoin.coinReddit!!)
        }


    }

    private fun coinStatistics() {
        binding.statistics.tvOpenAmount.text = dataInformation.dISPLAY.uSD.oPEN24HOUR
        binding.statistics.tvTodaysHighAmount.text = dataInformation.dISPLAY.uSD.hIGH24HOUR
        binding.statistics.tvTodayLowAmount.text = dataInformation.dISPLAY.uSD.lOW24HOUR
        binding.statistics.tvChangeTodayAmount.text = dataInformation.dISPLAY.uSD.cHANGE24HOUR
        binding.statistics.tvVolumeAlgorithm.text = dataInformation.coinInfo.algorithm
        binding.statistics.tvTotalVolume.text = dataInformation.dISPLAY.uSD.tOTALVOLUME24H
        binding.statistics.tvAvgMarketCapAmount.text = dataInformation.dISPLAY.uSD.mKTCAP
        binding.statistics.tvSupplyNumber.text = dataInformation.dISPLAY.uSD.sUPPLY
    }

    @SuppressLint("SetTextI18n")
    private fun chartShow() {
        var period: String = HOUR
        chartCoin(period)
        binding.chart.radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                R.id.radio_12h -> period = HOUR
                R.id.radio_1d -> period = HOURS24
                R.id.radio_1w -> period = WEEK
                R.id.radio_1m -> period = MONTH
                R.id.radio_3m -> period = MONTH3
                R.id.radio_1y -> period = YEAR
                R.id.radio_all -> period = ALL
            }
            chartCoin(period)

        }

        binding.chart.txtChartPrice.text = dataInformation.dISPLAY.uSD.pRICE
        binding.chart.txtChartChange1.text = " " + dataInformation.dISPLAY.uSD.cHANGE24HOUR


        val listCoin = listOf("Tether", "XRP","USD Coin")


        if (dataInformation.coinInfo.fullName in listCoin) {
            binding.chart.txtChartChange2.text = "0%"
        } else {
            binding.chart.txtChartChange2.text =
                dataInformation.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
        }





        val change = dataInformation.rAW.uSD.cHANGEPCT24HOUR
        if (change > 0) {

            binding.chart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.chart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.chart.txtChartUpdown.text = "▲"

            binding.chart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorGain
            )

        } else if (change < 0) {

            binding.chart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.chart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.chart.txtChartUpdown.text = "▼"

            binding.chart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context,
                R.color.colorLoss
            )


        }





        binding.chart.sparkviewMain.setScrubListener {
            if (it == null) {

                binding.chart.txtChartPrice.text = dataInformation.dISPLAY.uSD.pRICE

            } else {

                binding.chart.txtChartPrice.text = "$" + (it as ChartData.Data).close.toString()


            }
        }


    }

    private fun chartCoin(period: String) {

        apiManager.getChart(
            dataInformation.coinInfo.name,
            period,
            object : ApiManager.ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {
                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())

                    binding.chart.sparkviewMain.adapter = chartAdapter
                }

                override fun onError(error: String) {
                    Toast.makeText(this@CoinActivity, error, Toast.LENGTH_SHORT).show()
                }

            })


    }


}

