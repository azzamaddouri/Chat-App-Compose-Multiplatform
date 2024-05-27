import dev.icerock.moko.mvvm.viewmodel.ViewModel
import features.chat.domain.ChatRepository
import features.chat.domain.Message
import features.chat.presentation.event.GetMessagesEvent
import features.chat.presentation.event.SendMessageEvent
import features.chat.presentation.state.GetMessagesState
import features.chat.presentation.state.SendMessageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository): ViewModel() {
    private val _getMessagesState = MutableStateFlow(GetMessagesState())
    val getMessagesState = _getMessagesState.asStateFlow()

    private val _sendMessageState = MutableStateFlow(SendMessageState())
    val sendMessageState = _sendMessageState.asStateFlow()

    fun onGetMessagesEvent(event: GetMessagesEvent) {
        when (event) {
            is GetMessagesEvent.GetMessages -> getMessages()
        }
    }

    fun onSendMessageEvent(event: SendMessageEvent) {
        when (event) {
            is SendMessageEvent.SendMessage -> sendMessage(event.message)
        }
    }

    private fun getMessages() {
        if (_getMessagesState.value.isLoading) return

        _getMessagesState.update { GetMessagesState() }

        viewModelScope.launch {
            chatRepository.getMessages()
                .onSuccess { messageList ->
                    _getMessagesState.update {
                        it.copy(
                            isLoading = false,
                            messageList = messageList,
                        )
                    }
                }
                .onFailure {
                    _getMessagesState.update {
                        it.copy(
                            isLoading = false,
                            isFailure = true,
                        )
                    }
                }
        }
    }

    private fun sendMessage(message: Message) {
        if (_sendMessageState.value.isLoading) return

        _sendMessageState.update { SendMessageState() }

        viewModelScope.launch {
            chatRepository.sendMessage(message)
                .onSuccess {
                    // Update the local state with the new message
                    _getMessagesState.update { state ->
                        state.copy(
                            isLoading = false,
                            messageList = state.messageList + message
                        )
                    }
                    _sendMessageState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                        )
                    }
                }
                .onFailure {
                    _sendMessageState.update {
                        it.copy(
                            isLoading = false,
                            isFailure = true,
                        )
                    }
                }
        }
    }
}
