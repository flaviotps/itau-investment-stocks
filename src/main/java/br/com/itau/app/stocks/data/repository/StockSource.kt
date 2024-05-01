package br.com.itau.app.stocks.data.repository

import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData

interface StockSource {
    suspend fun listStocks(): NetworkResult<List<StockData>>
}