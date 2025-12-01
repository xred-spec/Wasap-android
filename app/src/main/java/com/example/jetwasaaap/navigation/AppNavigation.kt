package com.example.jetwasaaap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetwasaaap.ui.views.chat
import com.example.jetwasaaap.ui.views.login

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var darkTheme by remember { mutableStateOf(false) }
    var idActual by remember { mutableStateOf(0) }
    NavHost(navController = navController, startDestination = "login") {

        // RUTA 1: Login
        composable("login") {
            login(
                darkThemeState = darkTheme,
                onLoginSuccess = { nuevoEstadoTema, idUsuario ->
                    darkTheme = nuevoEstadoTema
                    idActual = idUsuario
                    navController.navigate("chat") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("chat") {
            chat(
                darkTheme = darkTheme,
                idUsuarioActual = idActual
            )
        }
    }
}