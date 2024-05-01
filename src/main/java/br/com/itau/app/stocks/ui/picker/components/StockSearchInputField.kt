package br.com.itau.app.stocks.ui.picker.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.ui_components.text.AutoCompleteTextView
import br.com.itau.app.ui_components.text.StockText

@Composable
fun StockSearchInputField (
    stocks: List<StockData>,
    onItemClick: (StockData) -> Unit

) {
    var searchInput by remember { mutableStateOf("") }

    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = searchInput,
        queryLabel = "Pesquisar",
        filter = { stock ->
            stock.name.contains(
                searchInput,
                ignoreCase = true
            ) or stock.code.contains(searchInput, ignoreCase = true)
        },
        onQueryChanged = { search ->
            searchInput = search
        },
        predictions = stocks,
        onClearClick = {
            searchInput = ""
        },
        onItemClick = { stock ->
            searchInput = "${stock.name} ( ${stock.code} )"
            onItemClick(stock)
        }
    ) { stock ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${stock.name} (${stock.code})")
            Spacer(modifier = Modifier.width(8.dp))
            StockText(value = stock.dailyVariation, showArrow = false)
        }
    }
}

