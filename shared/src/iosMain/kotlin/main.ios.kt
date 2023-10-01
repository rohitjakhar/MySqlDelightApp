import androidx.compose.ui.window.ComposeUIViewController
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.db.MyDatabase

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App() }

actual open class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MyDatabase.Schema, "test.db")
    }

}