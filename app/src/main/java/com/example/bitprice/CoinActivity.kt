package com.example.bitprice


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bitprice.apiManager.classData.CoinItem
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.databinding.ActivityCoinBinding


@Suppress("DEPRECATION")
class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataInformation: CoinsData.Data
    private lateinit var aboutCoin: CoinItem
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
        chartCoin()
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
                Toast.makeText(this, "GitHub URL not available", Toast.LENGTH_SHORT).show()
            }
        }



        binding.about.txtTwitter.setOnClickListener {
            if (!aboutCoin.coinTwitter.isNullOrEmpty() && aboutCoin.coinTwitter !="no-data") {
                val twitterHandle = "@" + aboutCoin.coinTwitter
                val twitterUrl = TWITTER_URL + aboutCoin.coinTwitter

                binding.about.txtTwitter.text = twitterHandle

                openLink(twitterUrl)
            } else {
                Toast.makeText(this, "Twitter URL not available", Toast.LENGTH_SHORT).show()
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


    private fun chartCoin() {

    }
}