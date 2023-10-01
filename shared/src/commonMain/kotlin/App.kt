import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.cash.sqldelight.Query
import app.cash.sqldelight.db.SqlDriver
import com.example.db.MyDatabase
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skiko.currentNanoTime
import kotlin.random.Random

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val dbFact = object : DatabaseDriverFactory() {}
        val myDb = createDatabase(dbFact)
        val players = remember { mutableStateOf<String?>("World!") }
        val scope = rememberCoroutineScope()
        val isChange = remember { mutableStateOf(1) }
        myDb.playerQueries.selectAll().addListener(object :Query.Listener{
            override fun queryResultsChanged() {
                isChange.value = isChange.value+1
            }
        })
        LaunchedEffect(isChange) {
            try {
                players.value = myDb.playerQueries.selectAll().executeAsList().random().eventName
            } catch (e: Exception){}
        }
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${players.value}"
                showImage = !showImage
                scope.launch {
                    myDb.playerQueries.insertEvent(
                        eventName = "Event Test ${Random.nextInt(100)}",
                        eventType = "Test",
                        attributes = "att",
                        trackedAt = currentNanoTime(),
                        Random.nextLong()
                    )
                }
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }
}

expect fun getPlatformName(): String

fun createDatabase(driverFactory: DatabaseDriverFactory): MyDatabase {
    val driver = driverFactory.createDriver()
    val database = MyDatabase(driver)
    return database
}

expect open class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}