/**
 * Grafo de navegação Compose: login → principal, abas e chat com argumento [conversationId].
 */
package br.com.fiap.wtchat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.fiap.wtchat.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {


        composable(route = "Login") {

            LoginScreen(navController = navController)
        }


        composable(route = "Principal") {
            MainScreen(navController = navController)
        }


        composable(route = "Conversas") {
            ConversasScreen(navController = navController)
        }

        composable(route = "Chat/{conversationId}") { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                ?: ""
            ChatScreen(navController = navController, conversationId = conversationId)
        }

        composable(route = "criar_lista") {
            CriarListasScreen(navController = navController)
        }

        composable(route = "criar_grupo") {
            CriarGrupoScreen(navController = navController)
        }

        composable(route = "criar_tarefa") {
            CriarTarefaScreen(navController = navController)
        }

        composable(route = "criar_disparo") {
            CriarDisparoScreen(navController = navController)
        }

        composable(route = "criar_ligacao") {
            CriarLigacaoScreen(navController = navController)
        }

        composable(route = "criar_promocao") {
            CriarPromocaoScreen(navController = navController)
        }

        composable(route = "clientes") {
            ClientesScreen(navController = navController)
        }

        composable(route = "cliente_perfil/{clientId}") { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId").orEmpty()
            Profile360Screen(navController = navController, clientId = clientId)
        }

        composable(route = "listas_api") {
            ApiListsScreen(navController = navController)
        }

        composable(route = "upload_midia") {
            MediaUploadScreen(navController = navController)
        }

        composable(route = "Ajustes") {
            ConfiguracoesScreen(navController = navController)
        }

        composable(route = "ajustes_conta") {
            SettingsDetailScreen(
                navController = navController,
                title = "Conta",
                icon = Icons.Default.Person,
                description = "Gerencie seus dados de perfil e informações do operador.",
                options = listOf("Nome do usuário conectado", "Perfil de acesso no CRM", "Dados usados no atendimento")
            )
        }

        composable(route = "ajustes_privacidade") {
            SettingsDetailScreen(
                navController = navController,
                title = "Privacidade",
                icon = Icons.Default.Lock,
                description = "Controle opções de segurança e uso dos dados dentro do app.",
                options = listOf("Sessão protegida por token", "Dados sincronizados apenas com o backend WTC", "Limpeza de sessão disponível no logout futuro")
            )
        }

        composable(route = "ajustes_notificacoes") {
            SettingsDetailScreen(
                navController = navController,
                title = "Notificações",
                icon = Icons.Default.Notifications,
                description = "Acompanhe alertas de conversas, tarefas e campanhas.",
                options = listOf("Token FCM registrado após login", "Alertas de novas mensagens", "Alertas de tarefas pendentes")
            )
        }

        composable(route = "ajustes_armazenamento") {
            SettingsDetailScreen(
                navController = navController,
                title = "Armazenamento e Dados",
                icon = Icons.Default.Storage,
                description = "Veja como o app usa dados locais e sincronização.",
                options = listOf("Conversas carregadas pela API", "Mídias e anexos serão conectados ao módulo de media", "Dados principais ficam no Firebase/Firestore")
            )
        }

        composable(route = "ajustes_trocar_email") {
            SettingsDetailScreen(
                navController = navController,
                title = "Trocar email",
                icon = Icons.Default.Email,
                description = "Área reservada para atualização de email da conta.",
                options = listOf("Validar email atual", "Informar novo email", "Confirmar alteração pelo backend")
            )
        }

        composable(route = "ajustes_trocar_senha") {
            SettingsDetailScreen(
                navController = navController,
                title = "Trocar senha",
                icon = Icons.Default.Lock,
                description = "Área reservada para atualização segura da senha.",
                options = listOf("Informar senha atual", "Criar nova senha", "Confirmar nova senha")
            )
        }

        composable(route = "ajustes_ajuda") {
            SettingsDetailScreen(
                navController = navController,
                title = "Ajuda e feedback",
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                description = "Central de suporte para dúvidas do operador.",
                options = listOf("Verificar conexão com a API", "Enviar feedback do app", "Consultar instruções de uso")
            )
        }

        composable(route = "Tarefas") {
            TarefasScreen(navController = navController)
        }


    }
}

@Composable
fun MessageScreen(navController: NavHostController) {

}
