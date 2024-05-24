import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import features.authentication.data.firebase.Firebase
import features.authentication.presentation.AuthScreen


@Composable
fun App() {
    val API_KEY = "AIzaSyAoMlUjpPAkVjZLhoOI8phPhFPsa5DsOnU"
    val DATABASE_URL = "https://fitnessconnect-3e757-default-rtdb.firebaseio.com"
    val firebase = Firebase()
    firebase.initialize(apiKey = API_KEY, databaseUrl = DATABASE_URL)
    MaterialTheme {
            Navigator(screen = AuthScreen())
    }
}