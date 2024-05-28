package features.home.presentation.state

import features.chat.domain.Message

data class GetAllMessagesState(
    val isLoading: Boolean = false,
    val messageList: List<Message> = emptyList(),
    val isFailure: Boolean = false
)