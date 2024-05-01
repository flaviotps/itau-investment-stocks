package br.com.itau.app.stocks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.stocks.data.repository.StockRepository
import br.com.itau.app.stocks.ui.picker.StockPickerState
import br.com.itau.app.stocks.ui.picker.StocksPickerViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StocksPickerViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StocksPickerViewModel
    private lateinit var stocksRepository: StockRepository
    private lateinit var stateObserver: Observer<StockPickerState>

    @Before
    fun setup() {
        stocksRepository = mockk()
        stateObserver = mockk(relaxed = true)
        viewModel = StocksPickerViewModel(stocksRepository)
        viewModel.state.observeForever(stateObserver)
    }

    @Test
    fun `listStocks() with network success`() = runBlocking {

        val stocks = listOf(
            StockData("AAPL", "Apple Inc.", 5f, 150.0f),
            StockData("GOOGL", "Alphabet Inc.", 5f, 2500.0f)
        )
        coEvery { stocksRepository.listStocks() } returns NetworkResult.Success(stocks)


        viewModel.listStocks()


        verify {
            stateObserver.onChanged(
                match {
                    it is StockPickerState.OnStocksLoaded && it.stocks == stocks
                }
            )
        }
    }


    @Test
    fun `listAssets network error`() = runBlocking {

        val errorMessage = "Network error"
        val networkResult = NetworkResult.Error<List<StockData>>(400, errorMessage)
        coEvery { stocksRepository.listStocks() } returns networkResult


        viewModel.listStocks()


        verify {
            stateObserver.onChanged(
                match {
                    it is StockPickerState.OnNetworkError && it.message == errorMessage
                }
            )
        }
    }

    @Test
    fun `listStocks() with network exception`() = runBlocking {

        val exceptionMessage = "Exception error"
        val exception = Throwable(exceptionMessage)
        coEvery { stocksRepository.listStocks() } returns NetworkResult.Exception(exception)

        viewModel.listStocks()

        verify {
            stateObserver.onChanged(
                match {
                    it is StockPickerState.OnExceptionError && it.exception.message == exceptionMessage
                }
            )
        }
    }
}
