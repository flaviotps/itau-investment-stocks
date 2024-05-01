package br.com.itau.app.stocks.ui.picker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StocksPickerViewModel(
    private val stocksRepository: StockRepository
) : ViewModel() {

    private val _state = MutableLiveData<StockPickerState>()
    val state: LiveData<StockPickerState> = _state

    fun listStocks() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = stocksRepository.listStocks()) {
                is NetworkResult.Success -> {
                    _state.postValue(StockPickerState.OnStocksLoaded(response.data))
                }

                is NetworkResult.Error -> {
                    _state.postValue(StockPickerState.OnNetworkError(response.message.orEmpty()))
                }

                is NetworkResult.Exception -> {
                    _state.postValue(StockPickerState.OnExceptionError(response.exception))
                }
            }
        }
    }
}