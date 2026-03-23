package br.com.fiap.wtchat.functions

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {

    val navBarColor = Color(0xFF093666)


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val items = listOf(
        Triple("Principal", Icons.Default.Home, "Principal"),
        Triple("Conversas", Icons.Default.Forum, "Conversas"),
        Triple("Criar", Icons.Default.AddCircle, "criar_lista"),
        Triple("Tarefas", Icons.Default.FilterList, "Tarefas"),
        Triple("Ajustes", Icons.Default.Settings, "Ajustes")
    )

    NavigationBar(containerColor = navBarColor) {

        items.forEach { (label, icon, route) ->


            val isSelected = currentRoute == route

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = isSelected,

                onClick = {

                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },


                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFEA5020),
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White,

                    indicatorColor = if (isSelected) Color(0x20FFFFFF) else Color.Transparent
                )
            )
        }
    }
}

