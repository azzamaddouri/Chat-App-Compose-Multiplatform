package features.authentication.presentation

sealed interface AuthEvent {
    data class SignUp(val email: String, val password: String, val confirmPassword: String) : AuthEvent
    data class SignIn(val email: String, val password: String) : AuthEvent
}