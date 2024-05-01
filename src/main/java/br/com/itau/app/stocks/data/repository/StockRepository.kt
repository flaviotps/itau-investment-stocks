package br.com.itau.app.stocks.data.repository

import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData

class StockRepository(
    private val stockRemoteDataSource: StockSource,
    private val stockLocalDataSource: StockSource
) {
    suspend fun listStocks(): NetworkResult<List<StockData>> {
        val result = stockRemoteDataSource.listStocks()
        if (result !is NetworkResult.Success) {
            return stockLocalDataSource.listStocks()
        }
        return result
    }
}