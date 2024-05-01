package br.com.itau.app.stocks.ui.alert.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.itau.app.stocks.R
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.ui_components.text.StockText

/**
 * Composable function for displaying a card containing stock alert information.
 *
 * @param stockData The stock data to be displayed in the alert card.
 * @param onInvalidInput Callback function to handle invalid input.
 * @param onValueChange Callback function to handle changes in the alert price value.
 */
@Composable
fun StockAlertCard(
    stockData: StockData,
    onInvalidInput: () -> Unit,
    onValueChange: (Float) -> Unit,
) {

    var newAlertPriceValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "${stockData.name} - ${stockData.code}",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.price, stockData.price),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.width(24.dp))
                StockText(value = stockData.dailyVariation)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newAlertPriceValue,
                    onValueChange = { newValue ->
                        newAlertPriceValue = newValue
                        handleValueChange(newValue, onValueChange, onInvalidInput)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(stringResource(R.string.preco_alvo)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun handleValueChange(
    newValue: String,
    onValueChange: (Float) -> Unit,
    onInvalidInput: () -> Unit
) {
    try {
        val floatValue = newValue.toFloat()
        onValueChange(floatValue)
    } catch (e: NumberFormatException) {
        onInvalidInput()
    }
}
