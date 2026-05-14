package br.com.fiap.wtchat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtchat.R
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.CampaignDTO
import br.com.fiap.wtchat.api.dto.SegmentDTO
import br.com.fiap.wtchat.api.dto.TaskDTO
import br.com.fiap.wtchat.auth.SessionManager
import br.com.fiap.wtchat.functions.BottomNavBar
import br.com.fiap.wtchat.ui.theme.WTChatTheme
import kotlinx.coroutines.launch

private val createOptions = listOf(
    CreateOption("Criar grupo", "Organize contatos por perfil, status ou campanha.", Icons.Default.Group, "criar_grupo"),
    CreateOption("Criar tarefa", "Registre uma pendência com prioridade e prazo.", Icons.Default.CalendarToday, "criar_tarefa"),
    CreateOption("Criar disparo", "Prepare uma mensagem para vários clientes.", Icons.AutoMirrored.Filled.Send, "criar_disparo"),
    CreateOption("Criar ligação", "Agende uma ligação e acompanhe o retorno.", Icons.Default.Call, "criar_ligacao"),
    CreateOption("Criar promoção", "Monte uma oferta comercial para envio.", Icons.Default.Campaign, "criar_promocao")
)

private data class CreateOption(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

private data class FormField(
    val label: String,
    val placeholder: String,
    val singleLine: Boolean = true
)

@Composable
fun CriarListasScreen(navController: NavController, modifier: Modifier = Modifier) {
    var search by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.branco_wtc))
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            CreateHeader(
                title = "Criar",
                search = search,
                onSearchChange = { search = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "O que você deseja criar?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.preto_wtc)
                )

                createOptions.forEach { option ->
                    CreateOptionCard(
                        option = option,
                        onClick = { navController.navigate(option.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun CriarGrupoScreen(navController: NavController) {
    CreateFormScreen(
        navController = navController,
        title = "Criar grupo",
        icon = Icons.Default.Group,
        submitLabel = "Salvar grupo",
        fields = listOf(
            FormField("Nome do grupo", "Ex: Clientes VIP"),
            FormField("Descrição", "Objetivo do grupo", singleLine = false),
            FormField("Visibilidade", "Equipe, privado ou publico"),
            FormField("Filtro inicial", "Ex: Leads ativos")
        ),
        onSubmit = { values ->
            val session = SessionManager.session ?: error("Faça login novamente para criar grupos.")
            val name = values.getOrNull(0).orEmpty().trim()
            if (name.isBlank()) error("Informe o nome do grupo.")

            RetrofitInstance.api.createSegment(
                authorization = "Bearer ${session.token}",
                request = SegmentDTO(
                    name = name,
                    description = buildDetails(
                        values.getOrNull(1).orEmpty(),
                        "Visibilidade" to values.getOrNull(2).orEmpty(),
                        "Filtro inicial" to values.getOrNull(3).orEmpty()
                    ),
                    color = "#EA5020"
                )
            )
            "Grupo criado no backend"
        }
    )
}

@Composable
fun CriarTarefaScreen(navController: NavController) {
    CreateFormScreen(
        navController = navController,
        title = "Criar tarefa",
        icon = Icons.Default.CalendarToday,
        submitLabel = "Salvar tarefa",
        fields = listOf(
            FormField("Titulo", "Ex: Retornar contato do cliente"),
            FormField("Responsavel", "Nome do operador"),
            FormField("Prazo", "Hoje, amanhã ou data especifica"),
            FormField("Prioridade", "Alta, media ou baixa"),
            FormField("Observações", "Detalhes da tarefa", singleLine = false)
        ),
        onSubmit = { values ->
            val session = SessionManager.session ?: error("Faça login novamente para criar tarefas.")
            val title = values.getOrNull(0).orEmpty().trim()
            if (title.isBlank()) error("Informe o titulo da tarefa.")

            val responsavel = values.getOrNull(1).orEmpty().trim()
            val prazo = values.getOrNull(2).orEmpty().trim()
            val prioridade = values.getOrNull(3).orEmpty().trim()
            val observacoes = values.getOrNull(4).orEmpty().trim()
            val description = buildTaskDescription(responsavel, prazo, observacoes)

            RetrofitInstance.api.createTask(
                authorization = "Bearer ${session.token}",
                request = TaskDTO(
                    title = title,
                    description = description,
                    operatorId = session.userId,
                    status = "OPEN",
                    priority = priorityFromLabel(prioridade)
                )
            )
            "Tarefa criada no backend"
        }
    )
}

@Composable
fun CriarDisparoScreen(navController: NavController) {
    CreateFormScreen(
        navController = navController,
        title = "Criar disparo",
        icon = Icons.AutoMirrored.Filled.Send,
        submitLabel = "Programar disparo",
        fields = listOf(
            FormField("Nome da campanha", "Ex: Retomada de orçamento"),
            FormField("Publico", "Grupo ou filtro de clientes"),
            FormField("Canal", "WhatsApp, email ou SMS"),
            FormField("Mensagem", "Texto do disparo", singleLine = false),
            FormField("Agendamento", "Enviar agora ou data/hora")
        ),
        onSubmit = { values ->
            val session = SessionManager.session ?: error("Faça login novamente para criar disparos.")
            val title = values.getOrNull(0).orEmpty().trim()
            if (title.isBlank()) error("Informe o nome da campanha.")

            RetrofitInstance.api.createCampaign(
                authorization = "Bearer ${session.token}",
                request = CampaignDTO(
                    title = title,
                    description = buildDetails(
                        values.getOrNull(3).orEmpty(),
                        "Canal" to values.getOrNull(2).orEmpty(),
                        "Agendamento" to values.getOrNull(4).orEmpty()
                    ),
                    targetSegmentId = values.getOrNull(1).orEmpty().trim().ifBlank { null },
                    status = "DRAFT"
                )
            )
            "Disparo criado no backend"
        }
    )
}

@Composable
fun CriarLigacaoScreen(navController: NavController) {
    CreateFormScreen(
        navController = navController,
        title = "Criar ligação",
        icon = Icons.Default.Call,
        submitLabel = "Agendar ligação",
        fields = listOf(
            FormField("Cliente", "Nome ou telefone"),
            FormField("Motivo", "Ex: Follow-up comercial"),
            FormField("Data e hora", "Ex: 10/05 às 14:00"),
            FormField("Responsavel", "Operador"),
            FormField("Notas", "Contexto para a ligação", singleLine = false)
        ),
        onSubmit = { values ->
            val session = SessionManager.session ?: error("Faça login novamente para criar ligações.")
            val client = values.getOrNull(0).orEmpty().trim()
            val reason = values.getOrNull(1).orEmpty().trim()
            if (client.isBlank()) error("Informe o cliente da ligação.")

            RetrofitInstance.api.createTask(
                authorization = "Bearer ${session.token}",
                request = TaskDTO(
                    title = "Ligação: $client",
                    description = buildDetails(
                        reason,
                        "Data e hora" to values.getOrNull(2).orEmpty(),
                        "Responsavel" to values.getOrNull(3).orEmpty(),
                        "Notas" to values.getOrNull(4).orEmpty()
                    ),
                    operatorId = session.userId,
                    status = "OPEN",
                    priority = 2
                )
            )
            "Ligação criada como tarefa"
        }
    )
}

@Composable
fun CriarPromocaoScreen(navController: NavController) {
    CreateFormScreen(
        navController = navController,
        title = "Criar promoção",
        icon = Icons.Default.Campaign,
        submitLabel = "Salvar promoção",
        fields = listOf(
            FormField("Nome da promoção", "Ex: Semana WTC"),
            FormField("Oferta", "Ex: 15% de desconto"),
            FormField("Periodo", "Inicio e fim da campanha"),
            FormField("Publico alvo", "Clientes ativos, leads ou grupos"),
            FormField("Condições", "Regras da promoção", singleLine = false)
        ),
        onSubmit = { values ->
            val session = SessionManager.session ?: error("Faça login novamente para criar promoções.")
            val title = values.getOrNull(0).orEmpty().trim()
            if (title.isBlank()) error("Informe o nome da promoção.")

            RetrofitInstance.api.createCampaign(
                authorization = "Bearer ${session.token}",
                request = CampaignDTO(
                    title = title,
                    description = buildDetails(
                        values.getOrNull(1).orEmpty(),
                        "Periodo" to values.getOrNull(2).orEmpty(),
                        "Publico alvo" to values.getOrNull(3).orEmpty(),
                        "Condições" to values.getOrNull(4).orEmpty()
                    ),
                    targetSegmentId = values.getOrNull(3).orEmpty().trim().ifBlank { null },
                    status = "DRAFT"
                )
            )
            "Promoção criada no backend"
        }
    )
}

@Composable
private fun CreateFormScreen(
    navController: NavController,
    title: String,
    icon: ImageVector,
    submitLabel: String,
    fields: List<FormField>,
    onSubmit: (suspend (List<String>) -> String)? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val values = remember(fields) { fields.map { mutableStateOf("") } }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.branco_wtc))
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            CreateHeader(title = title)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = colorResource(id = R.color.laranja_wtc),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = title, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.preto_wtc)
                    )
                }

                fields.forEachIndexed { index, field ->
                    OutlinedTextField(
                        value = values[index].value,
                        onValueChange = { values[index].value = it },
                        label = { Text(field.label) },
                        placeholder = { Text(field.placeholder) },
                        singleLine = field.singleLine,
                        minLines = if (field.singleLine) 1 else 4,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.azul_wtc),
                            focusedLabelColor = colorResource(id = R.color.azul_wtc),
                            cursorColor = colorResource(id = R.color.azul_wtc),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.cinzaescuro_wtc))
                    ) {
                        Text("Voltar")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                isSubmitting = true
                                try {
                                    val message = onSubmit?.invoke(values.map { it.value })
                                        ?: "$title salvo localmente"
                                    snackbarHostState.showSnackbar(message)
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(e.message ?: "Não foi possível salvar.")
                                } finally {
                                    isSubmitting = false
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc))
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Save, contentDescription = submitLabel, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(submitLabel)
                        }
                    }
                }
            }
        }
    }
}

private fun priorityFromLabel(value: String): Int {
    return when (value.trim().lowercase()) {
        "alta", "alto", "3" -> 3
        "media", "média", "medio", "médio", "2" -> 2
        else -> 1
    }
}

private fun buildTaskDescription(responsavel: String, prazo: String, observacoes: String): String {
    return listOf(
        responsavel.takeIf { it.isNotBlank() }?.let { "Responsavel: $it" },
        prazo.takeIf { it.isNotBlank() }?.let { "Prazo: $it" },
        observacoes.takeIf { it.isNotBlank() }
    ).filterNotNull().joinToString("\n").ifBlank { "Sem descrição." }
}

private fun buildDetails(mainText: String, vararg details: Pair<String, String>): String {
    return buildList {
        mainText.trim().takeIf { it.isNotBlank() }?.let { add(it) }
        details.forEach { (label, value) ->
            value.trim().takeIf { it.isNotBlank() }?.let { add("$label: $it") }
        }
    }.joinToString("\n").ifBlank { "Sem descrição." }
}

@Composable
private fun CreateHeader(
    title: String,
    search: String = "",
    onSearchChange: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF003366))
            .padding(start = 18.dp, top = 58.dp, end = 18.dp, bottom = 20.dp)
    ) {
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.branco_wtc)
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChange,
            singleLine = true,
            placeholder = { Text("Pesquisar", color = Color.DarkGray) },
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Mic, contentDescription = "Microfone")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xEFFFFFFF),
                focusedContainerColor = Color(0xEFFFFFFF),
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.azul_wtc)
            ),
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Composable
private fun CreateOptionCard(option: CreateOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = colorResource(id = R.color.azul_wtc),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(option.icon, contentDescription = option.title, tint = Color.White)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(option.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(option.description, fontSize = 12.sp, color = colorResource(id = R.color.cinzaescuro_wtc))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Abrir ${option.title}")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
fun CriarListaScreenPreview() {
    WTChatTheme {
        CriarListasScreen(navController = rememberNavController())
    }
}
