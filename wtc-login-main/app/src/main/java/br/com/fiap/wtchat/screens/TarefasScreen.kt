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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import br.com.fiap.wtchat.functions.BottomNavBar
import br.com.fiap.wtchat.ui.theme.WTChatTheme

private enum class TaskFilter(val label: String) {
    Todas("Todas"),
    Hoje("Hoje"),
    Pendentes("Pendentes"),
    Concluidas("Concluidas")
}

private data class TaskItem(
    val id: Int,
    val title: String,
    val description: String,
    val due: String,
    val priority: String,
    val section: String,
    val color: Color,
    val startsDone: Boolean = false
)

private val sampleTasks = listOf(
    TaskItem(
        id = 1,
        title = "Responder leads sem retorno",
        description = "Separar conversas abertas ha mais de 24h.",
        due = "Hoje, 10:30",
        priority = "Alta",
        section = "Hoje",
        color = Color(0xFFFFD6CC)
    ),
    TaskItem(
        id = 2,
        title = "Atualizar campanha de locação",
        description = "Revisar status dos disparos programados.",
        due = "Hoje, 14:00",
        priority = "Media",
        section = "Hoje",
        color = Color(0xFFFFF0B8)
    ),
    TaskItem(
        id = 3,
        title = "Confirmar reunião com cliente",
        description = "Enviar confirmação e pauta antes da chamada.",
        due = "Amanhã, 09:00",
        priority = "Media",
        section = "Proximas",
        color = Color(0xFFD8E8FF)
    ),
    TaskItem(
        id = 4,
        title = "Validar login e notificações",
        description = "Teste concluido no emulador Android.",
        due = "Ontem",
        priority = "Baixa",
        section = "Concluidas",
        color = Color(0xFFDFF5D2),
        startsDone = true
    )
)

@Composable
fun TarefasScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf(TaskFilter.Todas) }
    val checkedStates = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            sampleTasks.forEach { put(it.id, it.startsDone) }
        }
    }

    val visibleTasks = sampleTasks.filter { task ->
        when (selectedFilter) {
            TaskFilter.Todas -> true
            TaskFilter.Hoje -> task.section == "Hoje"
            TaskFilter.Pendentes -> checkedStates[task.id] != true
            TaskFilter.Concluidas -> checkedStates[task.id] == true
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.branco_wtc))
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentPadding = PaddingValues(start = 18.dp, top = 44.dp, end = 18.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tarefas", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Organize pendências por status",
                            color = colorResource(id = R.color.cinzaescuro_wtc),
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = { navController.navigate("criar_tarefa") },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Criar tarefa", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text("Nova")
                    }
                }
            }

            item {
                TaskSummaryRow(
                    total = sampleTasks.size,
                    pending = checkedStates.count { !it.value },
                    done = checkedStates.count { it.value }
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskFilter.values().forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colorResource(id = R.color.azul_wtc),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            val grouped = visibleTasks.groupBy { task ->
                if (checkedStates[task.id] == true) "Concluidas" else task.section
            }

            grouped.forEach { (section, tasks) ->
                item {
                    Text(section, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        isDone = checkedStates[task.id] == true,
                        onToggle = { checkedStates[task.id] = checkedStates[task.id] != true }
                    )
                }
            }

            if (visibleTasks.isEmpty()) {
                item {
                    EmptyTaskState()
                }
            }
        }
    }
}

@Composable
private fun TaskSummaryRow(total: Int, pending: Int, done: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        TaskMetricCard("Total", total.toString(), Icons.Default.Schedule, Modifier.weight(1f))
        TaskMetricCard("Pendentes", pending.toString(), Icons.Default.WarningAmber, Modifier.weight(1f))
        TaskMetricCard("Feitas", done.toString(), Icons.Default.Check, Modifier.weight(1f))
    }
}

@Composable
private fun TaskMetricCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = label, tint = colorResource(id = R.color.laranja_wtc))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, fontSize = 11.sp, color = colorResource(id = R.color.cinzaescuro_wtc))
        }
    }
}

@Composable
private fun TaskCard(task: TaskItem, isDone: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = task.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isDone) colorResource(id = R.color.check_wtc) else Color.White)
                    .clickable(onClick = onToggle),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(Icons.Default.Check, contentDescription = "Concluida", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(task.description, fontSize = 12.sp, color = colorResource(id = R.color.cinzaescuro_wtc))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskPill(task.due)
                    TaskPill(task.priority)
                }
            }
        }
    }
}

@Composable
private fun TaskPill(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        color = colorResource(id = R.color.preto_wtc),
        modifier = Modifier
            .background(Color(0x66FFFFFF), RoundedCornerShape(50))
            .padding(horizontal = 9.dp, vertical = 4.dp)
    )
}

@Composable
private fun EmptyTaskState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nenhuma tarefa aqui", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Troque o filtro ou crie uma nova tarefa.",
                color = colorResource(id = R.color.cinzaescuro_wtc),
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
fun TarefasScreenPreview() {
    WTChatTheme {
        TarefasScreen(navController = rememberNavController())
    }
}
