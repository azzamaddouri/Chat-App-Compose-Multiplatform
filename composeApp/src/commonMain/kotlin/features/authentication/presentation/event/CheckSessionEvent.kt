package features.authentication.presentation.event

sealed interface CheckSessionEvent {
    data object CheckSession : CheckSessionEvent
}