import androidx.compose.ui.window.ComposeUIViewController
import com.chatapp.firebaseauthentication.IOSDatabaseDriverFactory

fun MainViewController() = ComposeUIViewController {
    val driverFactory = IOSDatabaseDriverFactory()
    App(driverFactory.createDriver())
}