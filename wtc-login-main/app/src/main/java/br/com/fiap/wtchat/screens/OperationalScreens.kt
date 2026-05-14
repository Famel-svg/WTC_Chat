package br.com.fiap.wtchat.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.wtchat.R
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.CampaignDTO
import br.com.fiap.wtchat.api.dto.ClientDTO
import br.com.fiap.wtchat.api.dto.Profile360DTO
import br.com.fiap.wtchat.api.dto.SegmentDTO
import br.com.fiap.wtchat.api.dto.TaskDTO
import br.com.fiap.wtchat.auth.SessionManager
import br.com.fiap.wtchat.functions.BottomNavBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Composable
fun ClientesScreen(navController: NavController) {
    var clients by remember { mutableStateOf<List<ClientDTO>>(emptyList()) }
    var selected by remember { mutableStateOf<ClientDTO?>(null) }
    var loading by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("ACTIVE") }
    var segmentId by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }

    fun loadClients() {
        val session = SessionManager.session ?: return
        scope.launch {
            loading = true
            try {
                clients = RetrofitInstance.api.getClients("Bearer ${session.token}")
            } catch (e: Exception) {
                snackbar.showSnackbar(e.message ?: "Erro ao carregar clientes.")
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadClients() }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.branco_wtc))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionTitle("Clientes")
                Text("CRUD real consumindo /api/clients", color = Color.DarkGray)
            }

            item {
                CardBlock {
                    Field("Nome", name) { name = it }
                    Field("Email", email) { email = it }
                    Field("Telefone", phone) { phone = it }
                    Field("Categoria", category) { category = it }
                    Field("Status", status) { status = it }
                    Field("Segmento ID", segmentId) { segmentId = it }
                    Field("Avatar URL", avatarUrl) { avatarUrl = it }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val session = SessionManager.session ?: return@Button
                                scope.launch {
                                    try {
                                        val request = ClientDTO(
                                            name = name,
                                            email = email,
                                            phone = phone,
                                            category = category,
                                            status = status,
                                            segmentId = segmentId.ifBlank { null },
                                            avatarUrl = avatarUrl.ifBlank { null }
                                        )
                                        if (selected?.id == null) {
                                            RetrofitInstance.api.createClient("Bearer ${session.token}", request)
                                            snackbar.showSnackbar("Cliente criado")
                                        } else {
                                            RetrofitInstance.api.updateClient("Bearer ${session.token}", selected!!.id!!, request)
                                            snackbar.showSnackbar("Cliente atualizado")
                                        }
                                        selected = null
                                        name = ""; email = ""; phone = ""; category = ""; status = "ACTIVE"; segmentId = ""; avatarUrl = ""
                                        loadClients()
                                    } catch (e: Exception) {
                                        snackbar.showSnackbar(e.message ?: "Erro ao salvar cliente.")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc))
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Salvar")
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(if (selected == null) "Cadastrar" else "Atualizar")
                        }
                        Button(onClick = { loadClients() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                        }
                    }
                }
            }

            if (loading) {
                item { CircularProgressIndicator() }
            }

            items(clients, key = { it.id ?: it.email ?: it.name.orEmpty() }) { client ->
                CardBlock {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(client.name ?: "Sem nome", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("${client.email.orEmpty()} • ${client.phone.orEmpty()}", color = Color.DarkGray)
                            Text("Status: ${client.status.orEmpty()} | Score: ${client.score}", fontSize = 12.sp)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(onClick = {
                                selected = client
                                name = client.name.orEmpty()
                                email = client.email.orEmpty()
                                phone = client.phone.orEmpty()
                                category = client.category.orEmpty()
                                status = client.status.orEmpty().ifBlank { "ACTIVE" }
                                segmentId = client.segmentId.orEmpty()
                                avatarUrl = client.avatarUrl.orEmpty()
                            }) { Text("Editar") }
                            Button(onClick = { client.id?.let { navController.navigate("cliente_perfil/$it") } }) {
                                Icon(Icons.Default.Person, contentDescription = "Perfil 360")
                            }
                            Button(onClick = {
                                val session = SessionManager.session ?: return@Button
                                val id = client.id ?: return@Button
                                scope.launch {
                                    RetrofitInstance.api.deleteClient("Bearer ${session.token}", id)
                                    loadClients()
                                }
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Profile360Screen(navController: NavController, clientId: String) {
    var profile by remember { mutableStateOf<Profile360DTO?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(clientId) {
        val session = SessionManager.session ?: return@LaunchedEffect
        try {
            profile = RetrofitInstance.api.getProfile360("Bearer ${session.token}", clientId)
        } catch (e: Exception) {
            error = e.message
        }
    }

    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.branco_wtc)).padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SectionTitle("Perfil 360") }
            error?.let { item { Text(it, color = Color.Red) } }
            profile?.client?.let { client ->
                item {
                    CardBlock {
                        Text(client.name ?: "Cliente", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text(client.email.orEmpty())
                        Text("Categoria: ${client.category.orEmpty()} | Status: ${client.status.orEmpty()} | Score: ${client.score}")
                    }
                }
            }
            item { ProfileList("Mensagens recentes", profile?.recentMessages?.map { "${it.status ?: "SENT"} • ${it.content}" }.orEmpty()) }
            item { ProfileList("Campanhas recentes", profile?.recentCampaigns?.map { "${it.status ?: "DRAFT"} • ${it.title.orEmpty()}" }.orEmpty()) }
            item { ProfileList("Tarefas abertas", profile?.openTasks?.map { "${it.status ?: "OPEN"} • ${it.title.orEmpty()}" }.orEmpty()) }
        }
    }
}

@Composable
fun ApiListsScreen(navController: NavController) {
    var campaigns by remember { mutableStateOf<List<CampaignDTO>>(emptyList()) }
    var segments by remember { mutableStateOf<List<SegmentDTO>>(emptyList()) }
    var tasks by remember { mutableStateOf<List<TaskDTO>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val session = SessionManager.session ?: return@LaunchedEffect
        try {
            val auth = "Bearer ${session.token}"
            campaigns = RetrofitInstance.api.getCampaigns(auth)
            segments = RetrofitInstance.api.getSegments(auth)
            tasks = RetrofitInstance.api.getTasks(auth)
        } catch (e: Exception) {
            error = e.message
        }
    }

    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.branco_wtc)).padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { SectionTitle("Listagens da API") }
            error?.let { item { Text(it, color = Color.Red) } }
            item { ProfileList("Campanhas", campaigns.map { "${it.status ?: "DRAFT"} • ${it.title.orEmpty()}" }) }
            item { ProfileList("Segmentos / grupos", segments.map { "${it.name.orEmpty()} • ${it.clientCount} clientes" }) }
            item { ProfileList("Tarefas", tasks.map { "${it.status ?: "OPEN"} • ${it.title.orEmpty()}" }) }
        }
    }
}

@Composable
fun MediaUploadScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedUrl by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedUri = uri
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.branco_wtc)).padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionTitle("Upload de imagens")
            Text("Selecione uma imagem e envie para /api/media/upload.")
            Button(onClick = { picker.launch("image/*") }) {
                Icon(Icons.Default.Image, contentDescription = "Selecionar")
                Spacer(modifier = Modifier.padding(3.dp))
                Text("Selecionar imagem")
            }
            Text("Arquivo: ${selectedUri?.lastPathSegment ?: "nenhum"}")
            Button(
                enabled = selectedUri != null && !loading,
                onClick = {
                    val session = SessionManager.session ?: return@Button
                    val uri = selectedUri ?: return@Button
                    scope.launch {
                        loading = true
                        try {
                            val part = withContext(Dispatchers.IO) { uriToPart(context, uri) }
                            val response = RetrofitInstance.api.uploadMedia("Bearer ${session.token}", part, "android")
                            uploadedUrl = response.url
                            snackbar.showSnackbar("Upload concluído")
                        } catch (e: Exception) {
                            snackbar.showSnackbar(e.message ?: "Erro no upload.")
                        } finally {
                            loading = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc))
            ) {
                Text(if (loading) "Enviando..." else "Enviar imagem")
            }
            uploadedUrl?.let {
                CardBlock {
                    Text("URL gerada", fontWeight = FontWeight.Bold)
                    Text(it, color = colorResource(id = R.color.azul_link_wtc))
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.preto_wtc))
}

@Composable
private fun CardBlock(content: @Composable Column.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun ProfileList(title: String, rows: List<String>) {
    CardBlock {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        if (rows.isEmpty()) {
            Text("Nenhum registro encontrado.", color = Color.DarkGray)
        } else {
            rows.forEach { Text(it, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) }
        }
    }
}

private fun uriToPart(context: Context, uri: Uri): MultipartBody.Part {
    val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    val body = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", file.name, body)
}
