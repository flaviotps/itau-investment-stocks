package br.com.itau.app.stocks.data.remote

import br.com.itau.app.stocks.data.model.StockData
import retrofit2.Response
import retrofit2.http.GET

interface StockRemoteService {
    @GET("stocks/list")
    suspend fun listStocks(): Response<List<StockData>>
}