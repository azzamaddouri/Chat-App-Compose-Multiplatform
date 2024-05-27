package features.chat.presentation.state

data class SendMessageState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean=false
)
