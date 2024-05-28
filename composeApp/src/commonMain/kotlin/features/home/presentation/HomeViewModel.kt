package features.home.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import features.home.presentation.event.GetMessagesEvent
import features.home.domain.MessageRepository
import features.home.presentation.state.GetAllMessagesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val messageRepository: MessageRepository) : ViewModel() {
    private val _getMessagesState = MutableStateFlow(GetAllMessagesState())
    val getMessagesState = _getMessagesState.asStateFlow()
    fun onGetMessagesEvent(event: GetMessagesEvent) {
        when (event) {
            is GetMessagesEvent.GetMessages -> getMessages()
        }
    }
    private fun getMessages() {
        if (_getMessagesState.value.isLoading) return

        _getMessagesState.update { GetAllMessagesState() }

        viewModelScope.launch {
            messageRepository.getAllMessages()
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

}