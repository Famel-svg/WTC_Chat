package br.com.fiap.wtchat.screens

import br.com.fiap.wtchat.functions.BottomNavBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtchat.R
import br.com.fiap.wtchat.functions.FiltroDropdown
import br.com.fiap.wtchat.functions.MessageHeader
import br.com.fiap.wtchat.functions.SearchOutlinedTextField
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtchat.ui.theme.WTChatTheme



@Composable
fun CriarListasScreen(navController: NavController, modifier: Modifier = Modifier) {


    var search by remember { mutableStateOf("")}
    var titulo by remember { mutableStateOf("")}
    var visibilidade by remember { mutableStateOf("")}


    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()

            .padding(bottom = paddingValues.calculateBottomPadding())
            .background(Color.White)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF003366))
                        .padding(15.dp),
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    Text(
                        text = "Criações",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.branco_wtc),
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    OutlinedTextField(
                        value = search,
                        onValueChange = {search = it},
                        singleLine = true,
                        label = {
                            Text(
                                text = ("Envie uma mensagem"),
                                color = colorResource(id = R.color.cinzaescuro_wtc),
                                fontSize = 11.sp
                            )
                        },
                        placeholder = { Text("Pesquisar", color = Color.Gray , fontSize = 16.sp) },
                        trailingIcon = { Icon(Icons.Default.Mic, contentDescription = "Microfone") },
                        modifier = Modifier.fillMaxWidth()
                            .heightIn(max = 80.dp)
                            .padding(vertical = 22.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0x6EFFFFFF),
                            focusedContainerColor = Color(0x6EFFFFFF),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    )
                }

                MessageHeader(navController = navController)
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lista),
                        contentDescription = "Ícone de lista",
                        modifier = Modifier
                            .size(size = 60.dp)
                            .padding(start = 12.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = ("Criar Listas"),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 20.dp ,),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ("Título"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.cinzaescuro_wtc)
                    )
                    Spacer(Modifier.width(68.dp))
                    SearchOutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp , end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ("Visibilidade"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.cinzaescuro_wtc)
                    )
                    Spacer(Modifier.width(20.dp))
                    SearchOutlinedTextField(
                        value = visibilidade,
                        onValueChange = { visibilidade = it },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    thickness = 1.dp,
                    color = colorResource(id = R.color.cinzaescuro_wtc)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp , end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ("Filtros"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = R.color.cinzaescuro_wtc)
                    )
                    Spacer(Modifier.width(68.dp))
                    FiltroDropdown()
                }
                Spacer(modifier = Modifier.height(35.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = ("Filtros ativados"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.cinzaescuro_wtc)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc))
                    ) {
                        Text(
                            text = ("Tipo de cliente")
                        )
                    }
                }
            }
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
