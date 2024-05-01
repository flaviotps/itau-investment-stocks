package br.com.itau.app.stocks.ui.picker.components

data class StockSearchInput(
    val name: String,
    val code: String,
    val dailyVariation: Float,
    val price: Float
)