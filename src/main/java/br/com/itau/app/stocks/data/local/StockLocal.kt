package br.com.itau.app.stocks.data.local

import android.app.Application
import br.com.itau.app.network.loadFromAssets
import br.com.itau.app.stocks.data.model.StockData
import com.google.gson.reflect.TypeToken


class StockLocal(private val application: Application) {
    private val typeToken = object : TypeToken<List<StockData>>() {}
    fun listStocks(): List<StockData> = loadFromAssets<List<StockData>>(application, "stocks.json", typeToken) ?: emptyList()
}