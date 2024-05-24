package features.authentication.presentation

data class AuthState(
    val email: String = "",
    val password: String = "",
    val token: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean=false,
)
