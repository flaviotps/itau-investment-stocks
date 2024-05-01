package br.com.itau.app.stocks.ui.picker

import br.com.itau.app.stocks.data.model.StockData

sealed class StockPickerState {
    data object Loading : StockPickerState()
    class OnStocksLoaded(val stocks: List<StockData>) : StockPickerState()
    class OnNetworkError(val message: String) : StockPickerState()
    class OnExceptionError(val exception: Throwable) : StockPickerState()
}