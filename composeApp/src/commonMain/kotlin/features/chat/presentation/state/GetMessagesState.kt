package features.chat.presentation.state

import features.chat.domain.Message

data class GetMessagesState(
    val isLoading: Boolean = true,
    val messageList: List<Message> = emptyList(),
    val isFailure: Boolean = false
)

