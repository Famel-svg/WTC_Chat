package br.com.fiap.wtchat.screens
import androidx.navigation.NavController
import br.com.fiap.wtchat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import br.com.fiap.wtchat.functions.BottomNavBar
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

val lightGrayBackground = Color(0xFFE9E8E8)
val cardBackground = Color.White
val techLeadGreen = Color(0xFF4CAF50)
val buttonRed = Color(0xFFEA5020)
val textPrimary = Color.Black
val textSecondary = Color.Gray

val ColorBar = Color(0xFFCDCDCD)


@Composable
fun ConfiguracoesScreen(
    navController: NavController,
) {

    Scaffold(

        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGrayBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)

        ) {

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Ajustes",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Envie uma mensagem", color = textSecondary) },
                    trailingIcon = { Icon(Icons.Default.Mic, contentDescription = "Busca por voz") },
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxWidth()
                        .height(35.dp) ,
                    shape = RoundedCornerShape(35)
                )
            }
            item { ProfileCard(name = "Lucas Campos Da Silva", rm = "RM 560672", role = "Tech lead") }
            item { SettingsMenuGroup() }
            item { AccountDetailsCard(email = "fiap2025@gmil.com", onchangeEmailClick = {}, onChangePasswordClick = {}) }
            item { HelpAndFeedbackButton(onClick = {}) }
            item { FooterText() }
        }
    }
}



@Composable
fun ProfileCard(name: String, rm: String, role: String) {
    Spacer(modifier = Modifier.height(10.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.group12),
                contentDescription = "Foto de Perfil",
                modifier = Modifier.size(60.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip(text = rm, color = textPrimary)
                    Chip(text = role, color = buttonRed)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 12.sp,
        modifier = Modifier

            .background(
                color = Color(0xFFE1E1E1),
                shape = RoundedCornerShape(50)
            )

            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun SettingsMenuGroup() {
    Spacer(modifier = Modifier.height(5.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
    ) {
        Column {
            SettingsMenuItem(icon = Icons.Default.Person, text = "Conta", onClick = {})
            Divider(color = lightGrayBackground)
            SettingsMenuItem(icon = Icons.Default.Lock, text = "Privacidade", onClick = {})
            Divider(color = lightGrayBackground)
            SettingsMenuItem(icon = Icons.Default.Notifications, text = "Notificações", onClick = {})
            Divider(color = lightGrayBackground)
            SettingsMenuItem(icon = Icons.Default.Storage, text = "Armazenamento e Dados", onClick = {})
        }
    }
}

@Composable
fun SettingsMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = textSecondary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, modifier = Modifier.weight(1f), color = textPrimary)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Ir para $text",
            tint = textSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun AccountDetailsCard(email: String, onchangeEmailClick: () -> Unit, onChangePasswordClick: () -> Unit) {
    Spacer(modifier = Modifier.height(10.dp))
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Detalhes da conta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 5.dp, bottom = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Email", fontSize = 15.sp, color = textSecondary)
                        Text(email, fontSize = 16.sp, color = textPrimary)
                    }
                    Button(onClick = onchangeEmailClick, colors = ButtonDefaults.buttonColors(containerColor = buttonRed )) {
                        Text("Trocar email")
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 5.dp), color = lightGrayBackground)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Senha", fontSize = 12.sp, color = textSecondary)
                        Text("••••••••••••", fontSize = 16.sp, color = textPrimary)

                    }
                    Button(onClick = onChangePasswordClick, colors = ButtonDefaults.buttonColors(containerColor = buttonRed)) {
                        Text("Trocar senha")
                    }
                }
            }
        }
    }
}

@Composable
fun HelpAndFeedbackButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(5.dp))
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ajuda", tint = textSecondary)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Ajuda e feedback", color = textPrimary)
        }
    }
}

@Composable
fun FooterText() {
    Text(
        text = "© 2025 — Todos os direitos reservados.\nEm caso de dúvidas, suporte ou solicitações, entre em contato pelo número RM 650872.",
        fontSize = 10.sp,
        color = textSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun ConfiguracoesScreenPreview() {

    ConfiguracoesScreen(navController = rememberNavController())
}