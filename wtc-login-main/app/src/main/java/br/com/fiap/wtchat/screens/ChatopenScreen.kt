/**
 * UI do chat em bolhas; integra [ChatViewModel] para carregar/enviar mensagens via API REST.
 */
package br.com.fiap.wtchat.screens

import androidx.navigation.NavController
import br.com.fiap.wtchat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TopAppBarDefaults
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtchat.ui.theme.WTChatTheme
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.wtchat.viewmodel.ChatViewModel



val colorUserBubble = Color(0xFFEA5020)
val colorOtherBubble = Color(0xFF093666)
val colorOtherText = Color.White
val colorBackgroundChat = Color(0xFFF5F5F5)



data class ContactDetails(
    val name: String,
    val unreadCount: Int,
    val photoId: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(contact: ContactDetails, navController: NavController) {
    TopAppBar(
        modifier = Modifier.height(125.dp),

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF003366),
            titleContentColor = Color.White
        ),

        title = {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = 25.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = contact.photoId),
                        contentDescription = contact.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(contact.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight().padding(top = 25.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                    Text(
                        text = contact.unreadCount.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color.Red)
                            .padding(horizontal = 5.dp, vertical = 0.5.dp)
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier.fillMaxHeight().padding(top = 25.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.PhotoCameraFront, contentDescription = "Chamada de Vídeo", tint = Color.Red)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Call, contentDescription = "Chamada de Voz", tint = Color.Red)
                    }
                }
            }
        }
    )
}



@Composable
fun ChatInputBar(onSendMessage: (String) -> Unit) {
    var currentText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF003366))
            .heightIn(max = 80.dp)
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { }) {
            Icon(Icons.Default.Add, contentDescription = "Anexar", tint = Color.White)
        }

        OutlinedTextField(
            value = currentText,
            onValueChange = { currentText = it },
            placeholder = { Text("Envie uma mensagem", color = Color.Gray) },
            trailingIcon = {

                IconButton(onClick = {
                    if (currentText.isNotBlank()) {
                        onSendMessage(currentText)
                        currentText = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar Mensagem",
                        tint = Color.Red
                    )
                }
            },
            modifier = Modifier.weight(1f).heightIn(max = 48.dp).padding(horizontal = 8.dp),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )
    }
}


@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean,
    time: String,
    isRead: Boolean
) {
    val bubbleColor = if (isUser) colorUserBubble else colorOtherBubble
    val textColor = Color.White

    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 16.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = bubbleShape,
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = message,
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(end = 40.dp)
                )

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                    )
                    Spacer(Modifier.width(4.dp))

                    if (isUser) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Lida",
                            tint = if (isRead) Color(0xFFC7E6FF) else Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}



/**
 * @param conversationId identificador da conversa (rota `Chat/{conversationId}`).
 */
@Composable
fun ChatScreen(
    navController: NavController,
    conversationId: String,
    viewModel: ChatViewModel = viewModel()
) {
    val jm = ContactDetails(name = "Contato", unreadCount = 0, photoId = R.drawable.image_5)

    LaunchedEffect(conversationId) {
        if (conversationId.isNotBlank()) {
            viewModel.connect(conversationId)
        }
    }

    Scaffold(
        topBar = { ChatTopBar(contact = jm, navController = navController) },
        bottomBar = { ChatInputBar(onSendMessage = viewModel::sendMessage) }
    ) { paddingValues ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorBackgroundChat)
        ) {

            Image(
                painter = painterResource(id = R.drawable.background_chat),
                contentDescription = "Fundo do Chat",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                reverseLayout = true,


                verticalArrangement = Arrangement.Top
            ) {


                items(viewModel.messages.reversed()) { message ->
                    ChatBubble(
                        message = message.text,
                        isUser = message.isUser,
                        time = message.timestamp,
                        isRead = true
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ChatScreenFullPreview() {
    WTChatTheme {
        ChatScreen(navController = rememberNavController(), conversationId = "preview-conversation-id")
    }
}