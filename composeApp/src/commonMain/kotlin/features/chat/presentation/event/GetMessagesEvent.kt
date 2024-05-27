package features.chat.presentation.event

sealed interface GetMessagesEvent {
    data object GetMessages:GetMessagesEvent
}