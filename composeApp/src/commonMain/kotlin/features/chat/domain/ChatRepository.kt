package features.chat.domain

interface ChatRepository {
    suspend fun sendMessage(message: Message):Result<Unit>
    suspend fun getMessages():Result<List<Message>>
}