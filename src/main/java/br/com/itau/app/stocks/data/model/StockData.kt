package br.com.itau.app.stocks.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockData(
    val name: String,
    val code: String,
    val dailyVariation: Float,
    val price: Float,
) : Parcelable