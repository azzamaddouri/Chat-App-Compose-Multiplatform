package features.chat.presentation.event

import features.chat.domain.Message

sealed interface SendMessageEvent {
    data class SendMessage(val message: Message) : SendMessageEvent
}