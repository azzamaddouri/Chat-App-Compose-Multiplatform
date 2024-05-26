package features.authentication.presentation.state

import comchatappfirebaseauthentication.User

data class CheckSessionState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean=false
)
