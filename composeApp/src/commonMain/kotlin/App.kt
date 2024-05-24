import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import authentication.AuthenticationView
import authentication.AuthenticationViewModel
import authentication.MainScreen
import authentication.MainScreenViewModel
import cafe.adriel.voyager.navigator.Navigator


@Composable
fun App() {

    val authenticationViewModel = AuthenticationViewModel()
    val mainScreenViewModel = MainScreenViewModel()

    MaterialTheme {
            Navigator(
                screen = AuthenticationView(authenticationViewModel),
                onBackPressed = { currentScreen -> true })

    }
}