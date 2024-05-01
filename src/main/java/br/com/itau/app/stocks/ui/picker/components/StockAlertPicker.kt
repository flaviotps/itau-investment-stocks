package br.com.itau.app.stocks.ui.picker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.itau.app.analytics.ItauAnalytics
import br.com.itau.app.stocks.data.model.StockAlertData
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.stocks.toStockAlertData
import br.com.itau.app.stocks.ui.alert.components.StockAlertCard
import br.com.itau.app.stocks.ui.picker.StockPickerState
import br.com.itau.app.stocks.ui.picker.StocksPickerViewModel

@Composable
fun StockAlertPicker(
    stocksPickerViewModel: StocksPickerViewModel,
    onClick: (StockAlertData) -> Unit
) {

    var selectedStock by remember { mutableStateOf<StockData?>(null) }
    var selectedValue by remember { mutableFloatStateOf(0f) }
    var buttonEnabled by remember { mutableStateOf(false) }


    val state by stocksPickerViewModel.state.observeAsState(initial = StockPickerState.Loading)

    LaunchedEffect(stocksPickerViewModel) {
        stocksPickerViewModel.listStocks()
    }

    when (val currentState = state) {
        is StockPickerState.OnStocksLoaded -> {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StockSearchInputField(currentState.stocks) { stock ->
                    selectedStock =
                        StockData(stock.name, stock.code, stock.dailyVariation, stock.price)
                    val params = mapOf(
                        "stock_name" to stock.name,
                        "stock_price" to stock.price
                    )
                    ItauAnalytics.logEvent(
                        ItauAnalytics.CLICK,
                        params
                    )
                }

                selectedStock?.let { stock ->
                    StockAlertCard(stock, onInvalidInput = {
                        buttonEnabled = false
                    }) {
                        selectedValue = it
                        buttonEnabled = true
                    }
                    Button(
                        enabled = buttonEnabled,
                        onClick = {
                            onClick(stock.toStockAlertData(selectedValue))
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Adicionar")
                    }
                }
            }
        }

        is StockPickerState.OnNetworkError -> {
            val errorMessage = currentState.message
            Text(text = errorMessage)
        }

        is StockPickerState.OnExceptionError -> {
            val exception = currentState.exception
            Text(text = "Falha ao carregar ativos: $exception")
        }

        StockPickerState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
        }
    }
}