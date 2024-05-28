package features.home.domain

import features.chat.domain.Message

interface MessageRepository {
    suspend fun getAllMessages():Result<List<Message>>
}