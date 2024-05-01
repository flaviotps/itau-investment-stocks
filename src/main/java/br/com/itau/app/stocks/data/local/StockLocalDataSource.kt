package br.com.itau.app.stocks.data.local

import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.network.model.handleLocal
import br.com.itau.app.stocks.data.repository.StockSource

class StockLocalDataSource(
    private val stockLocal: StockLocal
) : StockSource {
    override suspend fun listStocks(): NetworkResult<List<StockData>> {
        return handleLocal { stockLocal.listStocks() }
    }
}
