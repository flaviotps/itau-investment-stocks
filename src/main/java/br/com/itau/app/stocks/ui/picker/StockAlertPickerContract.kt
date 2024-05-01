package br.com.itau.app.stocks.ui.picker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import br.com.itau.app.stocks.data.model.StockAlertData

const val SELECTED_STOCK_KEY = "SELECTED_STOCK"
const val EDIT_STOCK_KEY = "EDIT_STOCK"

class StockAlertPickerContract : ActivityResultContract<StockAlertData?, StockAlertData?>() {
    override fun createIntent(context: Context, input: StockAlertData?): Intent {
        return Intent(context, StockAlertPickerActivity::class.java).apply {
            putExtra(EDIT_STOCK_KEY, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): StockAlertData? {
        if (resultCode == Activity.RESULT_OK) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(SELECTED_STOCK_KEY, StockAlertData::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent?.getParcelableExtra(SELECTED_STOCK_KEY) as? StockAlertData
            }
        }
        return null
    }
}