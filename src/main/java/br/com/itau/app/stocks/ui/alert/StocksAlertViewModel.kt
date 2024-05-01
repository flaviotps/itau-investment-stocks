package br.com.itau.app.stocks.ui.alert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.app.stocks.data.model.StockAlertData

class StocksAlertViewModel : ViewModel() {

    private val stocks = mutableListOf<StockAlertData>()

    private val _state = MutableLiveData<StockAlertState>()
    val state: LiveData<StockAlertState> = _state

    fun addStock(stock: StockAlertData) {
        val existingStockIndex = stocks.indexOfFirst { it.name == stock.name }
        if (existingStockIndex != -1) {
            stocks[existingStockIndex] = stock
        } else {
            stocks.add(stock)
        }
        _state.postValue(StockAlertState.OnStockListChanged(stocks))
    }

    fun deleteStock(stock: StockAlertData) {
        stocks.remove(stock)
        _state.postValue(StockAlertState.OnStockListChanged(stocks))
    }
}