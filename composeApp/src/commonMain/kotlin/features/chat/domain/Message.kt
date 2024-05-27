package features.chat.domain
import core.utils.Utils
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val sender: Sender,
    val text: String,
    val messageTime: String = Utils.currentTime(),
    val images: List<ByteArray> = emptyList()
) {
    companion object {
        val empty = Message(
            sender = Sender("", ""),
            text = "",
            messageTime = "",
            images = emptyList()
        )
    }
}
