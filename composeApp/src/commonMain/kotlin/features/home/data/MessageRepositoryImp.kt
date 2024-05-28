package features.home.data

import features.chat.data.firebaseDB.FirebaseDatabase
import features.chat.domain.Message
import features.chat.domain.Sender
import features.home.domain.MessageRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class MessageRepositoryImp(private val firebaseDatabase: FirebaseDatabase) : MessageRepository {
    override suspend fun getAllMessages(): Result<List<Message>> {
        return runCatching {
            val child = listOf("messages")
            val response = firebaseDatabase.readFirebaseDatabase(child, "")
            val jsonResponse = Json.parseToJsonElement(response)
            val messages = mutableListOf<Message>()

            for (key1 in jsonResponse.jsonObject) {
                for (key2 in key1.value.jsonObject) {
                    try {
                        val message = Json.decodeFromJsonElement<Message>(key2.value)
                        messages.add(message)
                    } catch (e: Exception) {
                        // Log or handle JSON decoding errors
                        println("Error decoding message: ${e.message}")
                    }
                }
            }
            messages
        }.onFailure {
            // Log Firebase read failure
            println("Firebase read failed: ${it.message}")
        }
    }
}
