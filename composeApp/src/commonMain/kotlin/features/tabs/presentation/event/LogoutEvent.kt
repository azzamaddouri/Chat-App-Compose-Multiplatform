package features.tabs.presentation.event


sealed interface LogoutEvent{
    data object logout : LogoutEvent
}
