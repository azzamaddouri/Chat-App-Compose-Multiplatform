package features.chat.data.repository

import features.chat.data.firebaseDB.FirebaseDatabase
import features.chat.domain.ChatRepository
import features.chat.domain.Message
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class ChatRepositoryImp(private val firebaseDatabase: FirebaseDatabase) : ChatRepository {
    override suspend fun sendMessage(message: Message): Result<Unit> {
        return runCatching {
            val child = listOf("messages", Clock.System.now().epochSeconds.toString())
            val map = hashMapOf<String, Any>(message.sender.uid to message)

            firebaseDatabase.updateFirebaseDatabase(child, map)
        }
    }

    override suspend fun getMessages(): Result<List<Message>> {
        return runCatching {
            val child = listOf("messages")
            val response = firebaseDatabase.readFirebaseDatabase(child, "")

            val listofMessage = mutableListOf<Message>()
            val jsonResponse = Json.parseToJsonElement(response)

            for (key1 in jsonResponse.jsonObject.values) {
                for (key2 in key1.jsonObject.values) {
                    val message = Json { ignoreUnknownKeys = true }.decodeFromJsonElement<Message>(key2)
                    listofMessage.add(message)
                }
            }

            listofMessage
        }
    }
}
