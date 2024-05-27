package features.chat.domain

import kotlinx.serialization.Serializable

@Serializable
data class Sender(
    val uid: String,
    val email: String
)