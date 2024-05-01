package br.com.itau.app.stocks.data.remote

import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.network.model.handleApi
import br.com.itau.app.stocks.data.repository.StockSource

class StockRemoteDataSource(
    private val stockRemoteService: StockRemoteService
) : StockSource {

    override suspend fun listStocks(): NetworkResult<List<StockData>> {
        return handleApi {
            stockRemoteService.listStocks()
        }
    }
}
