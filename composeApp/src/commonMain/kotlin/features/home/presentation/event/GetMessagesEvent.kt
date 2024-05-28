package features.home.presentation.event

sealed interface GetMessagesEvent {
    data object GetMessages:GetMessagesEvent
}