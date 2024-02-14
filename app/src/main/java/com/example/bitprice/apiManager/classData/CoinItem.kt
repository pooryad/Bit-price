package com.example.bitprice.apiManager.classData

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinItem(

    var coinWebsite: String? = "no-data",
    var coinGit: String? = "no-data",
    var coinTwitter: String? = "no-data",
    var coinDesc: String? = "no-data",
    var coinReddit: String? = "no-data"


) : Parcelable
