package br.com.itau.app.stocks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.itau.app.stocks.data.model.StockAlertData
import br.com.itau.app.stocks.ui.alert.StockAlertState
import br.com.itau.app.stocks.ui.alert.StocksAlertViewModel
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StocksAlertViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StocksAlertViewModel
    private lateinit var stateObserver: Observer<StockAlertState>

    @Before
    fun setup() {
        stateObserver = mockk(relaxed = true)
        viewModel = StocksAlertViewModel()
        viewModel.state.observeForever(stateObserver)
    }

    @Test
    fun `updates state with new stock`() = runBlocking {

        val stock = StockAlertData("AAPL", "Apple Inc.", 5f, 150f, 160f)

        viewModel.addStock(stock)

        verify {
            stateObserver.onChanged(
                match {
                    it is StockAlertState.OnStockListChanged && it.stocks.contains(stock)
                }
            )
        }
    }

    @Test
    fun `addStock() updates state with multiple stocks`() = runBlocking {

        val stock1 = StockAlertData("AAPL", "Apple Inc.", 5.0f, 150f, 160f)
        val stock2 = StockAlertData("GOOGL", "Alphabet Inc.", 5.0f, 150f, 160f)

        viewModel.addStock(stock1)
        viewModel.addStock(stock2)

        verify {
            stateObserver.onChanged(
                match {
                    it is StockAlertState.OnStockListChanged &&
                            it.stocks.contains(stock1) &&
                            it.stocks.contains(stock2)
                }
            )
        }
    }
}
