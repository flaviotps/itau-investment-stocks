package br.com.itau.app.stocks.ui.picker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.itau.app.network.NetworkClient
import br.com.itau.app.stocks.R
import br.com.itau.app.stocks.data.local.StockLocal
import br.com.itau.app.stocks.data.local.StockLocalDataSource
import br.com.itau.app.stocks.data.model.StockAlertData
import br.com.itau.app.stocks.data.remote.StockRemoteDataSource
import br.com.itau.app.stocks.data.remote.StockRemoteService
import br.com.itau.app.stocks.data.repository.StockRepository
import br.com.itau.app.stocks.toStockData
import br.com.itau.app.stocks.ui.alert.components.StockAlertCard
import br.com.itau.app.stocks.ui.picker.components.StockAlertPicker
import br.com.itau.app.ui_components.theme.ItauInvestmentsTheme

class StockAlertPickerActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createBackPressDispatcher()

        val stockData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EDIT_STOCK_KEY, StockAlertData::class.java)
        } else {
            intent.getParcelableExtra(EDIT_STOCK_KEY)
        }

        setContent {

            var selectedValue by remember { mutableFloatStateOf(0f) }
            var buttonEnabled by remember { mutableStateOf(false) }

            ItauInvestmentsTheme {
                val scaffoldState = rememberScaffoldState()
                navController = rememberNavController()

                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.assets)) },
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    },
                    content = { padding ->
                        if (stockData == null) {
                            DisplayStockPicker(padding)
                        } else {
                            Column(
                                modifier = Modifier
                                    .padding(padding.calculateTopPadding() + 16.dp)
                                    .fillMaxSize()
                                    .background(Color.White),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                StockAlertCard(stockData.toStockData(), onInvalidInput = {
                                    buttonEnabled = false
                                }) {
                                    selectedValue = it
                                    buttonEnabled = true
                                }
                                Button(
                                    enabled = buttonEnabled,
                                    onClick = {
                                        stockData.targetPrice = selectedValue
                                        sendIntentResult(
                                            stockData
                                        )
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(text = stringResource(R.string.add))
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun createBackPressDispatcher() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentBackStackEntry == null) {
                    finish()
                } else {
                    navController.popBackStack()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @Composable
    private fun DisplayStockPicker(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(padding.calculateTopPadding() + 16.dp)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val stockRemoteService = NetworkClient(StockRemoteService::class.java)
            val stockLocal = StockLocal(application)
            val stockRemoteDataSource = StockRemoteDataSource(stockRemoteService)
            val stockLocalDataSource = StockLocalDataSource(stockLocal)
            val stockRepository =
                StockRepository(stockRemoteDataSource, stockLocalDataSource)
            val stocksPickerViewModel: StocksPickerViewModel =
                viewModel(factory = StocksPickerViewModelFactory(stockRepository))
            StockAlertPicker(stocksPickerViewModel) {
                sendIntentResult(it)
            }
        }
    }

    private fun sendIntentResult(it: StockAlertData) {
        val resultIntent = Intent().apply {
            putExtra(SELECTED_STOCK_KEY, it)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
