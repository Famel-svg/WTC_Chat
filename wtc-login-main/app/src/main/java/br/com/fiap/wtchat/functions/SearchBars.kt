package br.com.fiap.wtchat.functions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtchat.R

@Composable
fun SearchOutlinedTextField(modifier: Modifier) {

    var searchText by remember { mutableStateOf("") }

    val colorCinzaEscuro = colorResource(id = R.color.cinzaescuro_wtc)

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 80.dp),

        label = {
            Text(
                text = "Criar uma mensagem",
                color = colorCinzaEscuro,
                fontSize = 11.sp
            )
        },
        placeholder = {
            Text(
                text = "Pesquisar",
                color = colorCinzaEscuro,
                fontSize = 14.sp
            )
        },


        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Microfone"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(id = R.color.cinzaescuro_wtc),
                unfocusedTextColor = colorResource(id = R.color.cinzaescuro_wtc),
                focusedContainerColor = (Color.White),
                unfocusedContainerColor = (Color.White),

                unfocusedBorderColor = colorResource(id = R.color.cinzaescuro_wtc),
                cursorColor = colorResource(id = R.color.cinzaescuro_wtc),
                focusedLabelColor = colorResource(id = R.color.cinzaescuro_wtc),
                unfocusedLabelColor = colorResource(id = R.color.cinzaescuro_wtc)
        ),


        shape = MaterialTheme.shapes.extraLarge
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroDropdown() {

    var expanded by remember { mutableStateOf(false) }

    val opcoes = listOf(
        "Último acesso em",
        "Data da compra",
        "Tipo de cliente",
        "Maiores números de locação",
        "Criar novo filtro +"
    )


    var selecionado by remember { mutableStateOf("") }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            value = selecionado,
            onValueChange = { selecionado = it },
            label = { Text("Filtro") },
            placeholder = { Text("exemplo: Criado nos últimos 5 dias") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedTextColor = colorResource(id = R.color.preto_wtc),
                unfocusedTextColor = colorResource(id = R.color.cinzaescuro_wtc),
                focusedIndicatorColor = colorResource(id = R.color.cinzaescuro_wtc),
                unfocusedIndicatorColor = colorResource(id = R.color.cinzaescuro_wtc)
            )
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opcoes.forEach { opcao ->
                DropdownMenuItem(
                    text = { Text(opcao) },
                    onClick = {
                        selecionado = opcao
                        expanded = false
                    }
                )
            }
        }
    }
}
