package com.example.bitprice


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.databinding.ActivityCoinBinding

@Suppress("DEPRECATION")
class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataInformation: CoinsData.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dataInformation = intent.getParcelableExtra(DataTransfer)!!

        binding.toolbarCoin.toolbar.title = dataInformation.coinInfo.fullName
        uiCoin()


    }

    private fun uiCoin() {
        chartCoin()
        coinStatistics()
        coinAbout()
    }

    private fun coinAbout() {

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