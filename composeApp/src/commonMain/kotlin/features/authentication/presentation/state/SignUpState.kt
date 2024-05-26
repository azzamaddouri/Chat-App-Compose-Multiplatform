package features.authentication.presentation.state

data class SignUpState(
    val token: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean=false
)
