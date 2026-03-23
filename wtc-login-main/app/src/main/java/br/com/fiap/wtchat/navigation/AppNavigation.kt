/**
 * Grafo de navegação Compose: login → principal, abas e chat com argumento [conversationId].
 */
package br.com.fiap.wtchat.navigation

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

        composable(route = "Ajustes") {
            ConfiguracoesScreen(navController = navController)
        }

        // Placeholder until dedicated TaskList screen is integrated.
        composable(route = "Tarefas") {
            MainScreen(navController = navController)
        }


    }
}

@Composable
fun MessageScreen(navController: NavHostController) {

}