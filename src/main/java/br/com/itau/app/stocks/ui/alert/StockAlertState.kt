package br.com.itau.app.stocks.ui.alert

import br.com.itau.app.stocks.data.model.StockAlertData

sealed class StockAlertState {
    class OnStockListChanged(val stocks: List<StockAlertData>) : StockAlertState()
}