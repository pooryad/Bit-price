package com.example.bitprice


import com.example.bitprice.apiManager.classData.ChartData
import com.robinhood.spark.SparkAdapter

class ChartAdapter(private val historyData: List<ChartData.Data>, private val baseLine: String?) :
    SparkAdapter() {
    override fun getCount() = historyData.size


    override fun getItem(index: Int): ChartData.Data {
        return historyData[index]
    }

    override fun getY(index: Int): Float {
        return historyData[index].close.toFloat()
    }


    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine?.toFloat() ?: super.getBaseLine()
    }
}