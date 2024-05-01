package br.com.itau.app.stocks

import br.com.itau.app.network.model.NetworkResult
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.stocks.data.repository.StockRepository
import br.com.itau.app.stocks.data.repository.StockSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class StockRepositoryTest {

    @Test
    fun listStocks_ReturnsRemoteData_Success() {

        val stockRemoteDataSource: StockSource = mockk()
        val stockLocalDataSource: StockSource = mockk()

        val stockRepository = StockRepository(stockRemoteDataSource, stockLocalDataSource)

        val remoteStocks =
            listOf(StockData("1", "Stock A", 1f, 10f), StockData("2", "Stock B", 2f, 20f))
        coEvery { stockRemoteDataSource.listStocks() } returns NetworkResult.Success(remoteStocks)

        val result = runBlocking { stockRepository.listStocks() }

        val resultData = (result as NetworkResult.Success).data
        assertEquals(NetworkResult.Success(remoteStocks).data, resultData)

        coVerify(exactly = 0) { stockLocalDataSource.listStocks() }
    }

    @Test
    fun listStocks_ReturnsLocalData_Success() {

        val stockRemoteDataSource: StockSource = mockk()
        val stockLocalDataSource: StockSource = mockk()

        val stockRepository = StockRepository(stockRemoteDataSource, stockLocalDataSource)

        val localStocks =
            listOf(StockData("1", "Stock A", 1f, 10f), StockData("2", "Stock B", 2f, 20f))
        coEvery { stockRemoteDataSource.listStocks() } returns NetworkResult.Error(
            400,
            "failed to fetch from remote"
        )
        coEvery { stockLocalDataSource.listStocks() } returns NetworkResult.Success(localStocks)

        val result = runBlocking { stockRepository.listStocks() }

        val resultData = (result as NetworkResult.Success).data
        assertEquals(NetworkResult.Success(localStocks).data, resultData)

        coVerify(exactly = 1) { stockLocalDataSource.listStocks() }
        coVerify(exactly = 1) { stockLocalDataSource.listStocks() }
    }

    @Test
    fun listStocks_ReturnsLocalData_Error() {
        val stockRemoteDataSource: StockSource = mockk()
        val stockLocalDataSource: StockSource = mockk()

        val stockRepository = StockRepository(stockRemoteDataSource, stockLocalDataSource)

        coEvery { stockRemoteDataSource.listStocks() } returns NetworkResult.Error(
            400,
            "failed to fetch from remote"
        )
        coEvery { stockLocalDataSource.listStocks() } returns NetworkResult.Exception(Exception("Error"))

        val result = runBlocking { stockRepository.listStocks() }

        val resultData = (result as NetworkResult.Exception).exception.message
        assertEquals(resultData, result.exception.message)

        coVerify(exactly = 1) { stockLocalDataSource.listStocks() }
        coVerify(exactly = 1) { stockLocalDataSource.listStocks() }
    }

    @Test
    fun listStocks_ReturnsRemoteData_Error() {

        val stockRemoteDataSource: StockSource = mockk()
        val stockLocalDataSource: StockSource = mockk()

        val stockRepository = StockRepository(stockRemoteDataSource, stockLocalDataSource)

        val error =
            NetworkResult.Error<List<StockData>>(code = 400, "Failed to fetch remote stocks")
        coEvery { stockRemoteDataSource.listStocks() } returns error

        val localStocks =
            listOf(StockData("3", "Stock C", 3f, 30f), StockData("4", "Stock D", 4f, 40f))
        coEvery { stockLocalDataSource.listStocks() } returns NetworkResult.Success(localStocks)

        val result = runBlocking { stockRepository.listStocks() }

        val resultData = (result as NetworkResult.Success).data
        assertEquals(resultData, result.data)

        coVerify { stockLocalDataSource.listStocks() }
    }
}

