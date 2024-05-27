package features.tabs.presentation.state

data class LogoutState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean=false
)