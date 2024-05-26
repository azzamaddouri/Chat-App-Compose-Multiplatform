package features.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mainview.chat.ChatScreen
import mainview.chat.home.HomeScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

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


@OptIn(ExperimentalResourceApi::class)
data class TabsView(var mainScreenViewModel: TabsViewModel) : Screen {
    @Composable
    override fun Content() {
        val selectedIndex = remember { mutableStateOf(0) }
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)) {
                    Spacer(Modifier.weight(1f))

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
                0 -> HomeScreen()
                1 -> ChatScreen()
            }
        }
    }
}