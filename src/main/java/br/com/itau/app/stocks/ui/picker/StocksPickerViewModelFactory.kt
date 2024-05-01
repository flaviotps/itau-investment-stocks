package br.com.itau.app.stocks.ui.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.itau.app.stocks.data.repository.StockRepository

class StocksPickerViewModelFactory(private val stockRepository: StockRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StocksPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StocksPickerViewModel(stockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
