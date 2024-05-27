import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import features.authentication.data.firebase.Firebase
import features.chat.domain.Message
import features.chat.domain.Sender
import features.chat.presentation.components.ChatList
import features.chat.presentation.components.MessageInputBox
import features.chat.presentation.event.GetMessagesEvent
import features.chat.presentation.event.SendMessageEvent
import kotlinx.coroutines.launch
import androidx.compose.material.Scaffold
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import di.AppModule

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatView() {
    val uid = Firebase.getCurrentUser().uid
    val email = Firebase.getCurrentUser().emailID
    val viewModel = remember { ChatViewModel(AppModule.chatRepository) }

    val getMessagesState by viewModel.getMessagesState.collectAsState()
    val sendMessageState by viewModel.sendMessageState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onGetMessagesEvent(GetMessagesEvent.GetMessages)
    }

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            MessageInputBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 60.dp, top = 5.dp),
                onSendClick = { text ->
                    val message = Message(
                        sender = Sender(uid, email),
                        text = text
                    )
                    println("Click $message")
                    coroutineScope.launch {
                        viewModel.onSendMessageEvent(SendMessageEvent.SendMessage(message))
                    }
                }
            )
        }
    ) {
        ChatList(messages = getMessagesState.messageList)
    }
}
