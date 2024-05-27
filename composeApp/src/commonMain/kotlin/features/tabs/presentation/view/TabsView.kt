package features.tabs.presentation.view
import ChatView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.AppModule
import features.authentication.data.firebase.Firebase
import features.authentication.presentation.view.AuthView
import features.home.presentation.HomeView
import features.tabs.TabsViewModel
import features.tabs.presentation.event.LogoutEvent
import kotlinx.coroutines.launch

sealed class BottomNavigationScreen(val title:String) {
    object HomeScreen : BottomNavigationScreen("Home")

    object ChatScreen : BottomNavigationScreen("My Posts")

}

data class BottomNavigationItem(
    val route: String,
    val icon: ImageVector
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        BottomNavigationScreen.HomeScreen.title,
        Icons.Filled.Home
    ),
    BottomNavigationItem(
        BottomNavigationScreen.ChatScreen.title,
        Icons.Filled.Email
    )
)


class TabsView() : Screen {
    @Composable
    override fun Content() {
        val selectedIndex = remember { mutableStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        val currentUser = Firebase.getCurrentUser()

        val viewModel = remember { TabsViewModel(AppModule.UserRepository) }
        val logoutState by viewModel.logoutState.collectAsState()

        Scaffold(
            topBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)) {
                    Text(
                        text = "Current User: ${currentUser.emailID}",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp))
                    Spacer(Modifier.weight(1f))
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        onClick = {
                            coroutineScope.launch {
                                viewModel.onLogoutEvent(LogoutEvent.logout)
                                print("Logout button clicked")
                            }
                        }
                    ) {
                        Text("Logout")
                    }
                    if (logoutState.isSuccess) {
                        print("Logout state is successful")
                        navigator.push(AuthView())
                    }
                }
            },
            bottomBar = {
                BottomNavigation {
                    bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                        BottomNavigationItem(
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.Black,
                            label = {
                                Text(
                                    bottomNavigationItem.route
                                )
                            },
                            icon = {
                                Icon(bottomNavigationItem.icon, "")
                            },
                            selected = selectedIndex.value == index,
                            onClick = {
                                selectedIndex.value = index
                            }
                        )
                    }
                }
            }
        ) {
            when (selectedIndex.value) {
                0 -> HomeView()
                1 -> ChatView()
            }
        }
    }
}
