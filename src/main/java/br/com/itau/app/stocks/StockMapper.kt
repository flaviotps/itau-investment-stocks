package br.com.itau.app.stocks

import br.com.itau.app.stocks.data.model.StockAlertData
import br.com.itau.app.stocks.data.model.StockData


fun StockData.toStockAlertData(targetPrice: Float): StockAlertData {
    return StockAlertData(name, code, dailyVariation, price, targetPrice)
}

fun StockAlertData.toStockData(): StockData {
    return StockData(name, code, dailyVariation, price)
}