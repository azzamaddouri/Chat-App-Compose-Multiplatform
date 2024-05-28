package features.home.presentation.view

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import di.AppModule
import features.chat.presentation.components.ChatList
import features.home.presentation.HomeViewModel
import features.home.presentation.event.GetMessagesEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun HomeView() {
    val viewModel = remember { HomeViewModel(AppModule.messageRepository) }
    val getMessagesState by viewModel.getMessagesState.collectAsState()

    Scaffold {
        ChatList(messages= getMessagesState.messageList, _onRefresh = {
            viewModel.onGetMessagesEvent(GetMessagesEvent.GetMessages)
        })
    }
}