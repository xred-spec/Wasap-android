package com.example.jetwasaaap.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.gamesretrofit.network.RetrofitClient
import com.example.jetwasaaap.data.UsuariosRequest
import com.example.jetwasaaap.ui.theme.JetWasaaapTheme
import com.example.jetwasaaap.ui.theme.blueDark
import com.example.jetwasaaap.ui.theme.blueLight
import com.example.jetwasaaap.ui.theme.grayDark
import com.example.jetwasaaap.ui.theme.grayLight
import com.example.jetwasaaap.ui.theme.purpleDark
import com.example.jetwasaaap.ui.theme.purpleMid
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(
    onLoginSuccess: (Boolean, Int) -> Unit,
    darkThemeState: Boolean
) {
    val api = RetrofitClient.api
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var darkTheme by rememberSaveable { mutableStateOf(darkThemeState) }

    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    JetWasaaapTheme(darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Wasap") },
                    actions = {
                        IconButton(onClick = { darkTheme = !darkTheme }) {
                            Icon(
                                imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                tint = Color.White,
                                contentDescription = "Cambiar tema"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if(!darkTheme) blueLight
                        else purpleDark,
                            titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier.fillMaxSize().
                padding(innerPadding).
                padding(horizontal = 20.dp, vertical = 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if(!darkTheme) grayLight
                        else grayDark
                    ),
                    modifier = Modifier.padding(all = 30.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().
                        padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("Iniciar sesión", fontSize = 40.sp)

                        Spacer(modifier = Modifier.height(30.dp))
                        TextField(
                            value = userName,
                            onValueChange = { userName = it },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = if(!darkTheme) blueDark
                                else purpleMid,
                                cursorColor = if(!darkTheme) blueDark
                                else purpleMid,
                                focusedLabelColor = if(!darkTheme) blueDark
                                else purpleMid
                            ),
                            label = {Text("user name")},
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = if(!darkTheme) blueDark
                                else purpleMid,
                                cursorColor = if(!darkTheme) blueDark
                                else purpleMid,
                                focusedLabelColor = if(!darkTheme) blueDark
                                else purpleMid
                            ),
                            label = {Text("password")},
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if(!darkTheme) blueLight
                                else purpleDark,
                                contentColor = Color.White
                            ),
                            onClick = {
                                if(userName == "" || password == "") {
                                    Toast.makeText(context, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                scope.launch {
                                    val request = UsuariosRequest(
                                        username = userName,
                                        password = password
                                    )
                                    try {
                                        val response = api.login(request)
                                        if(response.isSuccessful) {
                                            val usuario = response.body()
                                            if(usuario != null) {
                                                Toast.makeText(context, "Bienvenido ${userName}", Toast.LENGTH_SHORT).show()
                                                onLoginSuccess(darkTheme, usuario.id)
                                                userName = ""
                                                password = ""
                                            } else {
                                                Toast.makeText(context, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "No se pudo establecer conexión", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                        ) {
                            Text(modifier = Modifier.padding(10.dp), text = "Ingresar", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}