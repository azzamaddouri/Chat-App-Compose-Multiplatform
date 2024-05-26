import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import app.cash.sqldelight.db.SqlDriver
import cafe.adriel.voyager.navigator.Navigator
import comchatappfirebaseauthentication.User
import di.AppModule
import features.authentication.data.firebase.Firebase
import features.authentication.data.firebase.FirebaseUser
import features.authentication.presentation.AuthViewModel
import features.authentication.presentation.event.CheckSessionEvent
import features.authentication.presentation.view.AuthView
import features.tabs.TabsView
import features.tabs.TabsViewModel

@Composable
fun App(sqlDriver: SqlDriver) {
    // Initialize Firebase and set up dependencies
    val firebase = remember { Firebase() }
    val API_KEY = "AIzaSyAoMlUjpPAkVjZLhoOI8phPhFPsa5DsOnU"
    val DATABASE_URL = "https://fitnessconnect-3e757-default-rtdb.firebaseio.com"
    firebase.initialize(apiKey = API_KEY, databaseUrl = DATABASE_URL)
    AppModule.setSqlDriver(sqlDriver)

    // ViewModels and state management
    val authViewModel = remember { AuthViewModel(AppModule.AuthRepository) }
    val mainScreenViewModel = remember { TabsViewModel() }
    val checkSessionState by authViewModel.checkSessionState.collectAsState()

    // State to manage session existence
    var sessionExist by remember { mutableStateOf(false) }
    var sessionChecked by remember { mutableStateOf(false) }

    // Trigger session check on composition start
    LaunchedEffect(Unit) {
        authViewModel.onCheckSessionEvent(CheckSessionEvent.CheckSession)
    }

    // React to changes in checkSessionState
    LaunchedEffect(checkSessionState) {
        when {
            checkSessionState.isLoading -> {
                // Handle loading state if needed
            }
            checkSessionState.isSuccess -> {
                val currentUser = checkSessionState.user
                if (currentUser != null) {
                    val firebaseUser = FirebaseUser(
                        currentUser.email, currentUser.idToken, currentUser.localId.toString()
                    )
                    firebase.setCurrentUser(firebaseUser)
                    sessionExist = true
                }
                sessionChecked = true
            }
            checkSessionState.isFailure -> {
                // Handle failure state if needed
                sessionExist = false
                sessionChecked = true
            }
        }
    }

    // Render the UI based on sessionExist state
    if (sessionChecked) {
        MaterialTheme {
            if (sessionExist) {
                Navigator(
                    screen = TabsView(mainScreenViewModel),
                    onBackPressed = { currentScreen -> true }
                )
            } else {
                Navigator(AuthView())
            }
        }
    } else {
        // Optionally show a splash screen or loading indicator here
    }
}
