package br.com.fiap.wtchat.screens
import br.com.fiap.wtchat.functions.BottomNavBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtchat.R
import br.com.fiap.wtchat.ui.theme.WTChatTheme
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.auth.SessionManager



data class Conversation(
    val id: String,
    val senderName: String,
    val senderRole: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val profilePicId: Int
)


val sampleNewMessage = Conversation(
    id = "preview",
    senderName = "Carlos",
    senderRole = "TL",
    lastMessage = "Está confirmado o layout.",
    timestamp = "Agora",
    unreadCount = 1,
    profilePicId = R.drawable.image14
)



@Composable
fun ConversasScreen(
    navController: NavController,
) {

    var isLoading by remember { mutableStateOf(true) }
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val session = SessionManager.session
            if (session == null) {
                isLoading = false
                return@LaunchedEffect
            }
            val auth = "Bearer ${session.token}"
            val apiConversations = RetrofitInstance.api.getConversations(auth)
            conversations = apiConversations.map { dto ->
                Conversation(
                    id = dto.id,
                    senderName = dto.name ?: dto.participantIds?.getOrNull(0) ?: "Contato",
                    senderRole = "",
                    lastMessage = dto.lastMessageContent ?: "",
                    timestamp = dto.lastMessageAt ?: "",
                    unreadCount = 0,
                    profilePicId = R.drawable.image_5
                )
            }
        } catch (e: Exception) {
            // Mantém lista vazia em caso de falha
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = { TopSection() },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            MessageHeader(navController = navController)
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (conversations.isEmpty()) {
                Text(
                    text = "Nenhuma conversa encontrada. Crie uma conversa pela API para iniciar o chat.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                ContactList(navController = navController, conversations = conversations)
            }
        }
    }
}



@Composable
fun TopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF003366))
            .padding(10.dp),
    )


    {

        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Conversas",
            fontSize = 40.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(1.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Pesquisar", color = Color.Gray, fontSize = 16.sp) },
            trailingIcon = { Icon(Icons.Default.Mic, contentDescription = "Microfone") },
            modifier = Modifier.fillMaxWidth()
                .heightIn(max = 80.dp)
                .padding(vertical = 20.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0x6EFFFFFF),
                focusedContainerColor = Color(0x6EFFFFFF),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            ),
            shape = MaterialTheme.shapes.extraLarge
        )

    }
}



@Composable
fun MessageHeader(navController: NavController) {
    val actions = listOf(
        Triple("Criar grupo", Icons.Default.Group, "criar_lista"),
        Triple("Criar tarefa", Icons.Default.CalendarToday, "Tarefas"),
        Triple("Disparo", Icons.Default.Send, "Conversas"),
        Triple("Criar ligação", Icons.Default.Call, "Principal"),
        Triple("Promoção", Icons.Default.Campaign, "Principal")
    )
    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 14.dp)
    ) {
        items(actions) { (text, icon, route) ->
            Button(
                onClick = { navController.navigate(route) },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA5020))
            ) {
                Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text, fontSize = 12.sp)
            }
        }
        item {
            Button(
                onClick = { navController.navigate("Principal") },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.size(56.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
            ) {}
        }
    }
}


@Composable
fun ContactList(navController: NavController, conversations: List<Conversation>) {

    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items(conversations) { convo ->
            ConversationItem(convo = convo, navController = navController)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationItem(convo: Conversation, navController: NavController) {
    Surface(
        onClick = {
            navController.navigate("Chat/${convo.id}")
        },
        color = Color(0xFFE2E2E2),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = convo.profilePicId),
                contentDescription = "Foto de ${convo.senderName}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)

            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("${convo.senderName} (${convo.senderRole})", fontWeight = FontWeight.Bold , fontSize = 17.sp)
                Text(convo.lastMessage, fontSize = 15.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(convo.timestamp, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                if (convo.unreadCount > 0) {
                    Badge(containerColor = Color(0xFFD32F2F)) {
                        Text(
                            text = convo.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
fun ConversasScreenPreview() {
    WTChatTheme {
        ConversasScreen(navController = rememberNavController())
    }
}