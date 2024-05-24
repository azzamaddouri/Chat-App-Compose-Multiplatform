package features.authentication.domain

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val expiresIn: String,
    val tokenType: String,
    val refreshToken: String,
    val idToken: String,
    val userId: String,
    val projectId: String
)
