package br.com.fiap.wtchat.functions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.wtchat.R
import androidx.navigation.NavController

@Composable
fun ItemDeTarefa(
    texto: String,
    cor: androidx.compose.ui.graphics.Color,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .height(30.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = cor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = texto,
                color = colorResource(id = R.color.preto_wtc),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )


            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) colorResource(id = R.color.check_wtc)
                        else colorResource(id = R.color.unchecked_wtc)
                    )
                    .clickable { onCheckedChange(!isChecked) },
            )
        }
    }
}




@Composable
fun CardDeReuniao(
    titulo: String,
    hora: String,
    participante: String,
    ocupacao: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 4.dp)
            .background(colorResource(id = R.color.cinzabarra_wtc)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.cinzabarra_wtc)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.preto_wtc),
                    fontSize = 14.sp
                )

                Text(
                    text = hora,
                    color = colorResource(id = R.color.laranja_wtc),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nome
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.preto_wtc),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = participante,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.preto_wtc)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))


                Box(
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.verde_botao),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = ocupacao,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.preto_wtc)
                    )
                }
            }
        }
    }
}



@Composable
fun MessageHeader(navController: NavController) {
    val actions = listOf(

        Triple("Criar grupo", Icons.Default.Group, "criar_grupo_route"),
        Triple("Criar tarefa", Icons.Default.CalendarToday, "tarefas"),
        Triple("Disparo", Icons.Default.Send, "disparo_route"),
        Triple("Criar ligação", Icons.Default.Call, "ligacao_route"),
        Triple("promoção", Icons.Default.Campaign, "promocao_route")
    )

    LazyRow(
        modifier = Modifier
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) {

        items(actions) { (text, icon, route) ->
            Button(
                onClick = {

                    navController.navigate(route)
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.laranja_wtc))
            ) {
                Icon(icon, contentDescription = text, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text, fontSize = 12.sp)
            }
        }


        item {
            Button(
                onClick = {
                    navController.navigate("Principal")
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.size(56.dp, 36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
            ) { }
        }
    }
}


