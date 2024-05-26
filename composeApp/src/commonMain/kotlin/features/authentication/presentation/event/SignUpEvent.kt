package features.authentication.presentation.event

sealed interface SignUpEvent {
    data class SignUp(val email: String, val password: String, val confirmPassword: String) : SignUpEvent

}