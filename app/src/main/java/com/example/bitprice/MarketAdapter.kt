package com.example.bitprice

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitprice.apiManager.classData.CoinsData
import com.example.bitprice.databinding.ItemRecyclerBinding

class MarketAdapter(
    private val data: ArrayList<CoinsData.Data>,
    private val recyclerCallback: RecyclerCallback
) :
    RecyclerView.Adapter<MarketAdapter.MarketHolder>() {
    private lateinit var binding: ItemRecyclerBinding

    inner class MarketHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun coinView(dataCoin: CoinsData.Data) {
            if (dataCoin.rAW != null && dataCoin.dISPLAY != null) {

                binding.txtCoinName.text = dataCoin.coinInfo.fullName
                binding.txtPrice.text = dataCoin.dISPLAY.uSD.pRICE


                val marketCap = dataCoin.rAW.uSD.mKTCAP / 1000000000

                val dot = marketCap.toString().indexOf('.')
                binding.txtMarketCap.text = "$" + marketCap.toString().substring(0 , dot + 3) + " B"


                val change = dataCoin.rAW.uSD.cHANGEPCT24HOUR
                if (change > 0) {
                    binding.txtCoinBase.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorGain
                        )
                    )
                    binding.txtCoinBase.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"
                } else if (change < 0) {

                    binding.txtCoinBase.setTextColor(
                        (ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorLoss
                        ))
                    )
                    binding.txtCoinBase.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
                } else {

                    binding.txtCoinBase.text = "0%"
                }



                Glide
                    .with(binding.root)
                    .load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                    .into(binding.imageView)

                itemView.setOnClickListener {
                    recyclerCallback.coinClick(dataCoin)
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketHolder {

        binding =
            ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketHolder(binding.root)
    }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: MarketHolder, position: Int) {
        holder.coinView(data[position])
    }


    interface RecyclerCallback {
        fun coinClick(dataCoin: CoinsData.Data)
    }
}