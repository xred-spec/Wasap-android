package com.example.jetwasaaap.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamesretrofit.network.RetrofitClient
import com.example.jetwasaaap.data.MensajesDTO
import com.example.jetwasaaap.data.MensajesRequest
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
fun chat(
    idUsuarioActual: Int,
    darkTheme: Boolean
) {
    var darkTheme by rememberSaveable { mutableStateOf(darkTheme) }
    val api = RetrofitClient.api
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var mensaje by remember { mutableStateOf("") }

    var listaMensajes by remember { mutableStateOf(listOf<MensajesDTO>()) }

    fun cargarMensajes() {
        scope.launch {
            try {
                val response = api.getMensajes()
                if (response.isSuccessful && response.body() != null) {
                    listaMensajes = response.body()!!
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al cargar chat", Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(Unit) {
        cargarMensajes()
    }

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

                        IconButton(onClick = { cargarMensajes()
                            Toast.makeText(context, "Chat actualizado", Toast.LENGTH_SHORT).show()}) {
                            Icon(
                                imageVector = Icons.Default.Loop,
                                tint = Color.White,
                                contentDescription = "Cargar mensajes"
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
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding).
                        weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(listaMensajes) { msg ->
                        val miUsuario = msg.usuario == idUsuarioActual
                        val nombreUsuario by produceState(initialValue = "...", key1 = msg.usuario) {
                                try {
                                    val response = api.getUsuario(msg.usuario)
                                    if (response.isSuccessful && response.body() != null) {
                                        value = response.body()!!.username
                                    }
                                } catch (e: Exception) {
                                    value = "Error"
                                }

                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (miUsuario) Arrangement.End else Arrangement.Start
                        ) {
                            Card(
                                modifier = Modifier.padding(vertical = 5.dp, horizontal = 5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (miUsuario) {
                                        if (!darkTheme) blueLight
                                        else purpleDark
                                    } else {
                                        if (!darkTheme) grayLight
                                        else grayDark
                                    }
                                ),
                                shape = if(miUsuario) RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp, bottomEnd = 15.dp)
                                        else RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp, bottomStart = 15.dp),
                                elevation = CardDefaults.cardElevation(2.dp),
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Text(
                                        fontWeight = FontWeight.Black,
                                        text = "${nombreUsuario}",
                                        color = if(miUsuario) {
                                            grayLight
                                        } else {
                                            if(!darkTheme) blueDark
                                            else purpleMid
                                        }

                                    )
                                    Text(
                                        text = msg.mensaje,
                                        color = if(!miUsuario) {
                                            if(!darkTheme) grayDark
                                            else grayLight
                                        } else {
                                            grayLight
                                        },
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().background(if(!darkTheme) blueLight
                    else purpleDark,).
                    padding(start = 10.dp, end = 10.dp,
                        bottom = 50.dp, top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = mensaje,
                        onValueChange = { mensaje = it },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if(!darkTheme) blueDark
                            else purpleMid,
                            cursorColor = if(!darkTheme) blueDark
                            else purpleMid,
                            focusedLabelColor = if(!darkTheme) blueDark
                            else purpleMid
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )

                    IconButton(onClick = {
                        if (mensaje.isBlank()) return@IconButton
                        scope.launch {
                            val request = MensajesRequest(
                                mensaje = mensaje,
                                usuario = idUsuarioActual
                            )
                            try {
                                val response = api.sendMensaje(request)
                                if (response.isSuccessful) {
                                    mensaje = ""
                                    cargarMensajes()
                                } else {
                                    Toast.makeText(context, "Error al enviar", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "No se pudo conectar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            tint = Color.White,
                            contentDescription = "Enviar mensaje"
                        )
                    }
                }
            }
        }
    }
}