package br.com.itau.app.stocks

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.filters.SmallTest
import br.com.itau.app.stocks.data.model.StockData
import br.com.itau.app.stocks.ui.alert.components.StockAlertCard
import org.junit.Rule
import org.junit.Test

@SmallTest
class StockAlertCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stockAlertCard_DisplayedData() {
        val stockData = StockData(
            name = "Company ABC",
            code = "ABC",
            dailyVariation = 5f,
            price = 100.0f
        )

        composeTestRule.setContent {
            StockAlertCard(stockData = stockData, onInvalidInput = { }, onValueChange = { })
        }

        composeTestRule.onNodeWithText("Company ABC - ABC").assertExists()

        composeTestRule.onNodeWithText("Preço: 100.0").assertExists()

        composeTestRule.onNodeWithText("Preço alvo").assertExists()
    }
}
