package br.com.itau.app.stocks.ui.alert

import ExpandableCard
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.itau.app.analytics.ItauAnalytics
import br.com.itau.app.stocks.R
import br.com.itau.app.stocks.data.model.StockAlertData
import br.com.itau.app.stocks.ui.picker.StockAlertPickerContract
import br.com.itau.app.ui_components.commons.formatCurrency
import br.com.itau.app.ui_components.text.StockText
import br.com.itau.app.ui_components.theme.Component.Color.lightGreen
import br.com.itau.app.ui_components.theme.Component.Color.middleGray


@Composable
fun StocksHeader(modifier: Modifier = Modifier, onClick: () -> Unit) {

    val walletProfitability by remember { mutableFloatStateOf(0f) }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxHeight()
                .width(1.dp)
        )
        StockText(value = walletProfitability)
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxHeight()
                .width(1.dp)
        )
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = stringResource(R.string.notifications),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(R.string.create_stock_alert),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable {
                onClick()
            })
    }
}

@Composable
fun Stocks(stocksViewModel: StocksAlertViewModel) {

    val state by stocksViewModel.state.observeAsState()

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {


        val launcher =
            rememberLauncherForActivityResult(StockAlertPickerContract()) { stockAlertData ->
                stockAlertData?.let {
                    stocksViewModel.addStock(it)
                }
            }

        Column {

            StocksHeader(modifier = Modifier.padding(vertical = 24.dp)) {
                launcher.launch(null)
            }

            Divider(thickness = 1.dp, color = middleGray)

            when (val currentState = state) {
                is StockAlertState.OnStockListChanged -> {
                    StocksAlertContent(currentState,
                        onDeleteClicked = {
                            ItauAnalytics.logEvent(
                                ItauAnalytics.SCREEN_NAME, mapOf(
                                    "Screen" to "StocksAlert",
                                    "Price" to it.price,
                                    "TargetPrice" to it.targetPrice,
                                    "Delete" to it.name
                                )
                            )
                            stocksViewModel.deleteStock(it)
                        }, onEditClicked = {
                            ItauAnalytics.logEvent(
                                ItauAnalytics.SCREEN_NAME, mapOf(
                                    "Screen" to "StocksAlert",
                                    "Price" to it.price,
                                    "TargetPrice" to it.targetPrice,
                                    "EDIT" to it.name
                                )
                            )
                            launcher.launch(it)
                        }
                    )
                }

                null -> NoAlertContent()
            }


        }
    }
}

@Composable
private fun NoAlertContent() {
    Text(
        text = stringResource(R.string.no_asset_added),
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(8.dp)
    )
}


@Composable
fun StocksAlertContent(
    state: StockAlertState.OnStockListChanged,
    onEditClicked: (StockAlertData) -> Unit,
    onDeleteClicked: (StockAlertData) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.stocks.size) { index ->
            ExpandableCard(
                initialState = true,
                titleContent = {
                    StockCardContent(
                        title = state.stocks[index].name,
                    )
                }
            ) {
                val stock = state.stocks[index]
                StockCardContent(
                    stock,
                    onEditClicked = { onEditClicked(stock) },
                    onDeleteClicked = { onDeleteClicked(stock) }
                )
            }
        }
    }
}


@Composable
private fun StockCardContent(
    stock: StockAlertData,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.target_price, stock.targetPrice.formatCurrency()),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDeleteClicked) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colors.error
            )
        }
        IconButton(onClick = onEditClicked) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit),
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun StockCardContent(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.6f),
            text = title,
            style = MaterialTheme.typography.h6,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .background(lightGreen)
                .padding(horizontal = 4.dp),
            color = Color.White,
            text = stringResource(R.string.stock_avaliable),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

