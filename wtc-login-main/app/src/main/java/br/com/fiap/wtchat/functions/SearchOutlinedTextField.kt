package br.com.fiap.wtchat.functions

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//
@Composable
fun SearchOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Pesquisar", color = Color.Gray, fontSize = 16.sp) },
        trailingIcon = { Icon(Icons.Default.Send, contentDescription = "Enviar") },

        modifier = modifier
            .height(30.dp)
            .heightIn(max = 20.dp),

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFCDCDCD),
            focusedContainerColor = Color(0xFFCDCDCD),

        ),
        shape = MaterialTheme.shapes.extraLarge
    )
}
