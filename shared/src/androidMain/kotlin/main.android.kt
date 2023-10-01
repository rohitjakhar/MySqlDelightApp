import android.content.Context
import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.db.MyDatabase

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()

actual open class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MyDatabase.Schema, context = context, "test.dp")
    }
}
