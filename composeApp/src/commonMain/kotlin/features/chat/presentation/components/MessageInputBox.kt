package features.chat.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalFoundationApi
@Composable
fun MessageInputBox(
    modifier: Modifier = Modifier,
    onSendClick: (String) -> Unit
) {
    val textState = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column {
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            maxLines = 3,
            placeholder = {
                Text(
                    text = "Type a message...",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                    ),
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {
                Button(
                    onClick = {
                        onSendClick(textState.value)
                        textState.value = ""
                    },
                    content = {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.rotate(-90.0F).size(20.dp),
                        )
                    },
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            },
            modifier = modifier,
            shape = RoundedCornerShape(24),
        )
    }
}