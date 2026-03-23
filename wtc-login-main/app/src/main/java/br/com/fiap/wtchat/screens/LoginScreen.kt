package br.com.fiap.wtchat.screens
import br.com.fiap.wtchat.R
import android.view.View.X
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.wtchat.ui.theme.WTChatTheme
import br.com.fiap.wtchat.api.RetrofitInstance
import br.com.fiap.wtchat.api.dto.LoginRequestDTO
import br.com.fiap.wtchat.auth.SessionManager
import br.com.fiap.wtchat.auth.SessionState
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(modifier: Modifier = Modifier,
        navController: NavController) {

    val waveShape = GenericShape { size, _ ->
        moveTo(0f, size.height * 0.7f)
        cubicTo(
            size.width * 0.95f, size.height * 0.00f,
            size.width * 0.0f, size.height * 0.95f,
            size.width, size.height * 0.7f
        )
        lineTo(size.width, 0f)
        lineTo(0f, 0f)
        close()
    }

    var username by remember { mutableStateOf("") }
    
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()
        .background(color = colorResource(id = R.color.branco_wtc))
    ) {

        Column( modifier = Modifier.fillMaxWidth()

        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp)
                    .clip(waveShape),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.laranja_wtc)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {}
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "WTC Logo",
                    modifier = Modifier
                        .size(330.dp)
                        .offset(y = (-80).dp)
                )
                Text(
                    text = ("WORLD TRADE CENTER®"),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.offset(y = (-160).dp)
                )
                Text(
                    text = ("SÃO PAULO"),
                    color = colorResource(id = R.color.laranja_wtc),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier = Modifier.offset(y = (-150).dp)
                )
                Text(
                    text = ("Usuário"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .offset(y = (-85).dp),
                    fontSize = 16.sp
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = {
                        Text(
                            "Digite o número funcional",
                            fontSize = 15.sp)
                            },
                    placeholder = {
                        Text(
                            "Digite o número funcional",
                            fontSize = 10.sp
                        )
                                  },
                    singleLine = true,
                    shape = RoundedCornerShape(size = 20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp )
                        .heightIn(min = 4.dp)
                        .offset(y = (-80).dp),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.azul_wtc),
                        unfocusedBorderColor = colorResource(id = R.color.azul_wtc),
                        cursorColor = colorResource(id = R.color.laranja_wtc)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier.height(35.dp))
                Text(
                    text = ("Senha"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .offset(y = (-85).dp),
                    fontSize = 16.sp
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            "Digite sua senha",
                            fontSize = 15.sp)
                    },
                    placeholder = {
                        Text(
                            "Digite sua senha",
                            fontSize = 10.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(size = 20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .offset(y = (-80).dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.azul_wtc),
                        unfocusedBorderColor = colorResource(id = R.color.azul_wtc),
                        cursorColor = colorResource(id = R.color.laranja_wtc)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Text(
                    text = ("Esqueci minha senha"),
                    color = colorResource(id = R.color.azul_link_wtc),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.offset(y = (-75).dp, x = 80.dp)
                )

                Button(
                    onClick = {
                        if (isLoading) return@Button
                        val email = username.trim()
                        val pass = password
                        if (email.isBlank() || pass.isBlank()) {
                            errorMessage = "Informe e-mail e senha."
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        scope.launch {
                            try {
                                val response = RetrofitInstance.api.login(
                                    LoginRequestDTO(email = email, password = pass)
                                )
                                SessionManager.setSession(
                                    SessionState(
                                        token = response.token,
                                        userId = response.userId,
                                        role = response.role,
                                        name = response.name
                                    )
                                )
                                navController.navigate("Principal") {
                                    popUpTo("Login") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Falha no login."
                            } finally {
                                isLoading = false
                            }
                        }
                    },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.laranja_wtc)
                    ),
                    modifier = Modifier
                        .width(140.dp)
                        .offset(y = (-50).dp)
                ) {
                    Text(
                        text = if (isLoading) "Entrando..." else "Entrar"
                    )
                }
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .offset(y = (-30).dp)
                    )
                }
                Box(contentAlignment = Alignment.BottomCenter) {
                    BottomWaveShape()
                }
                }
            }
        }
    }




@Composable
fun BottomWaveShape() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(0f, height * 0.4f)
            cubicTo(
                width * 0.25f, height * 0.6f,
                width * 0.75f, height * 0f,
                width, 0f
            )
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = path,
            color = Color(0xFF002B5B)
        )
    }
}



@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_6")
@Composable
fun LoginScreenPreview() {
    WTChatTheme {
        LoginScreen(navController = rememberNavController())
    }
}



